package com.backendTest.transfers.model;

import java.math.BigDecimal;

/**
 * Result of a reduction
 */
public class ReduceResult {

    /**
     * Whether successfull or not
     */
    private final boolean success;

    /**
     * Current balance of the account
     */
    private final BigDecimal currentBalance;

    ReduceResult(boolean success, BigDecimal currentBalance) {
        this.success = success;
        this.currentBalance = currentBalance;
    }

    /**
     * @return whether successfull or not
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return current balance
     */
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }
}
