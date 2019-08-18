package com.backendTest.transfers.service;

import com.backendTest.transfers.model.Account;
import com.backendTest.transfers.model.AccountStore;
import com.backendTest.transfers.model.ReduceResult;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * A coordinator of transactions
 */
public class TransactionCoordinator {

    private final AccountStore accountStore;

    public TransactionCoordinator(AccountStore accountStore) {
        this.accountStore = accountStore;
    }

    /**
     * Add an account with the given id
     * @param id is the id to add
     * @return Result of transaction. Possible values are <br/>
     * {@link TransactionResponseCode#SUCCESS} <br/>
     * {@link TransactionResponseCode#ACCOUNT_ALREADY_EXISTS}
     */
    public TransactionResponseCode addAccount(long id) {
        boolean success = accountStore.addAccount(id);
        return success ? TransactionResponseCode.SUCCESS : TransactionResponseCode.ACCOUNT_ALREADY_EXISTS;
    }

    /**
     * Add balance to account
     * @param id is the id of the account
     * @param value is the balance to add
     * @return Result of transaction. Possible valued are <br/>
     * {@link TransactionResponseCode#SUCCESS} <br/>
     * {@link TransactionResponseCode#ACCOUNT_NOT_FOUND}
     */
    public TransactionResponseCode addBalance(long id, BigDecimal value) {
        Optional<Account> account = getAccount(id);
        if (!account.isPresent()) {
            return TransactionResponseCode.ACCOUNT_NOT_FOUND;
        }
        account.get().add(value);
        return TransactionResponseCode.SUCCESS;
    }

    /**
     * A transfer from one account to another
     * @param fromAccountId is the account from with to transfer
     * @param toAccountId is the account to with to transfer
     * @param amount is the amount to transfer
     * @return {@link TransferResult} indicating the result of the transfer
     */
    public TransferResult transfer(long fromAccountId, long toAccountId, BigDecimal amount) {
        Optional<Account> fromAccount = getAccount(fromAccountId);
        if (!fromAccount.isPresent()) {
            return new TransferResult(TransactionResponseCode.ACCOUNT_NOT_FOUND, fromAccountId);
        }
        Optional<Account> toAccount = getAccount(toAccountId);
        if (!toAccount.isPresent()) {
            return new TransferResult(TransactionResponseCode.ACCOUNT_NOT_FOUND, toAccountId);
        }
        ReduceResult withdrawResult = fromAccount.get().withdraw(amount);
        if (!withdrawResult.isSuccess()) {
            return new TransferResult(TransactionResponseCode.INSUFFICIENT_BALANCE, fromAccountId);
        }
        toAccount.get().add(amount);
        return new TransferResult(TransactionResponseCode.SUCCESS, toAccountId);
    }

    /**
     * Get account with id
     * @param id is the id
     * @return Account if present, otherwise {@link Optional#empty()}
     */
    public Optional<Account> getAccount(long id) {
        return accountStore.getAccount(id);
    }
}
