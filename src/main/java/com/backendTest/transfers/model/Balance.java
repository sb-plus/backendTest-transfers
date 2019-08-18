package com.backendTest.transfers.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Account balance
 */
public class Balance {

    /**
     * Current balance
     */
    private final AtomicReference<BigDecimal> balance = new AtomicReference<>(BigDecimal.ZERO);

    Balance() {}

    /**
     * Adds specified value to the balance.
     * @param value is the value to add
     * @return new balance
     */
    public BigDecimal add(BigDecimal value) {
        return balance.accumulateAndGet(value, BigDecimal::add);
    }

    /**
     * Reduces balance with the specified amount
     * @param value is the value to reduce
     * @return {@link ReduceResult} representing result of reduction
     */
    public ReduceResult reduce(BigDecimal value) {
        while (true) {
            var current = balance.get();
            if (current.equals(BigDecimal.ZERO)) {
                return new ReduceResult(false, current);
            }
            var result = current.subtract(value);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                return new ReduceResult(false, current);
            }
            if (balance.compareAndSet(current, result)) {
                return new ReduceResult(true, result);
            }
        }
    }

    /**
     * @return current balance
     */
    public BigDecimal getCurrent() {
        return balance.get();
    }
}
