package com.backendTest.transfers.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class BalanceTest {

    private Balance balance;

    @Before
    public void beforeTest() {
        balance = new Balance();
    }

    @Test
    public void returnsZeroBBalanceForNew() {
        assertThat(balance.getCurrent(), is(BigDecimal.ZERO));
    }

    @Test
    public void addIncreasesBalance() {
        BigDecimal ten = new BigDecimal("10");
        BigDecimal result = balance.add(ten);
        assertThat(result, is(ten));
        assertThat(balance.getCurrent(), is(ten));
    }

    @Test
    public void reduceOnZeroBalanceFails() {
        ReduceResult result = balance.reduce(new BigDecimal("10"));
        assertFalse(result.isSuccess());
        assertThat(result.getCurrentBalance(), is(BigDecimal.ZERO));
    }

    @Test
    public void reduceOnLowBalanceFails() {
        BigDecimal ten = new BigDecimal("10");
        balance.add(ten);
        ReduceResult result = balance.reduce(new BigDecimal("15"));
        assertFalse(result.isSuccess());
        assertThat(result.getCurrentBalance(), is(ten));
    }

    @Test
    public void reduceOnEnoughBalanceSucceeds() {
        balance.add(new BigDecimal("10"));
        ReduceResult result = balance.reduce(new BigDecimal("2"));
        assertTrue(result.isSuccess());
        assertThat(result.getCurrentBalance(), is(new BigDecimal("8")));
        assertThat(balance.getCurrent(), is(new BigDecimal("8")));
    }
}