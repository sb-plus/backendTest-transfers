package com.backendTest.transfers.service;

/**
 * Result of a transfer
 */
public class TransferResult {

    private final TransactionResponseCode responseCode;

    /**
     * Relevant account id for the response
     */
    private final long accountId;

    TransferResult(TransactionResponseCode responseCode, long accountId) {
        this.responseCode = responseCode;
        this.accountId = accountId;
    }

    /**
     * @return the {@link TransactionResponseCode} for this result
     */
    public TransactionResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     * @return the account id on with the result is applicable
     */
    public long getAccountId() {
        return accountId;
    }
}
