package com.backendTest.transfers.service;

import com.backendTest.transfers.model.Account;
import com.backendTest.transfers.model.AccountStore;
import com.backendTest.transfers.model.ReduceResult;
import junit.framework.TestCase;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionCoordinatorTest {

    @Mock private AccountStore accountStore;
    @Mock private Account account1;
    @Mock private Account account2;
    @Mock private ReduceResult reduceResult;

    private long id1 = 10;
    private long id2 = 20;

    private TransactionCoordinator coordinator;

    @Before
    public void beforeTest() {
        when(accountStore.getAccount(id1)).thenReturn(Optional.of(account1));
        when(accountStore.getAccount(id2)).thenReturn(Optional.of(account2));
        when(account1.withdraw(any())).thenReturn(reduceResult);
        coordinator = new TransactionCoordinator(accountStore);
    }

    @Test
    public void addAccountCallsStore() {
        when(accountStore.addAccount(12)).thenReturn(true);
        TransactionResponseCode responseCode = coordinator.addAccount(12);
        assertThat(responseCode, is(TransactionResponseCode.SUCCESS));
        verify(accountStore).addAccount(12);
    }

    @Test
    public void addAccountFailsIfAccountAlreadyExists() {
        TransactionResponseCode responseCode = coordinator.addAccount(12);
        assertThat(responseCode, is(TransactionResponseCode.ACCOUNT_ALREADY_EXISTS));
    }

    @Test
    public void transferFailsIfFromAccountNotFound() {
        TransferResult result = coordinator.transfer(12, id2, new BigDecimal("10"));
        assertThat(result.getResponseCode(), is(TransactionResponseCode.ACCOUNT_NOT_FOUND));
        assertThat(result.getAccountId(), is(12l));
    }

    @Test
    public void transferFailsIfToAccountNotFound() {
        TransferResult result = coordinator.transfer(id1, 12, new BigDecimal("10"));
        assertThat(result.getResponseCode(), is(TransactionResponseCode.ACCOUNT_NOT_FOUND));
        assertThat(result.getAccountId(), is(12l));
    }

    @Test
    public void transferFailsIfFromAccountHasInsufficientBalance() {
        TransferResult result = coordinator.transfer(id1, id2, new BigDecimal("10"));
        assertThat(result.getResponseCode(), is(TransactionResponseCode.INSUFFICIENT_BALANCE));
        assertThat(result.getAccountId(), is(id1));
    }

    @Test
    public void transferSucceeds() {
        when(reduceResult.isSuccess()).thenReturn(true);
        BigDecimal amount = new BigDecimal("10");
        TransferResult result = coordinator.transfer(id1, id2, amount);
        assertThat(result.getResponseCode(), is(TransactionResponseCode.SUCCESS));
        verify(account1).withdraw(amount);
        verify(account2).add(amount);
    }

    @Test
    public void addBalanceFailsIfAccountNotFound() {
        TransactionResponseCode responseCode = coordinator.addBalance(12, new BigDecimal("10"));
        assertThat(responseCode, is(TransactionResponseCode.ACCOUNT_NOT_FOUND));
    }

    @Test
    public void addBalanceSucceeds() {
        BigDecimal ten = new BigDecimal("10");
        TransactionResponseCode responseCode = coordinator.addBalance(id1, ten);
        assertThat(responseCode, is(TransactionResponseCode.SUCCESS));
        verify(account1).add(ten);
    }


}