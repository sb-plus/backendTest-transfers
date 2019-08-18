package com.backendTest.transfers.model;

import java.math.BigDecimal;

public class Account {

    private final long id;

    private final Balance balance = new Balance();

    Account(long id) {
        this.id = id;
    }

    /**
     * Add balance to account
     * @param value balance to add
     * @return latest balance
     */
    public BigDecimal add(BigDecimal value) {
        return balance.add(value);
    }

    /**
     * Withdraw balance from account
     * @param value balance to withdraw
     * @return {@link ReduceResult} as result of withdraw
     */
    public ReduceResult withdraw(BigDecimal value) {
        return balance.reduce(value);
    }

    /**
     * Get current balance
     * @return current balance
     */
    public BigDecimal getCurrentBalance() {
        return balance.getCurrent();
    }

}
