package com.backendTest.transfers.service;

/**
 * List of possible responses to a transaction
 */
public enum TransactionResponseCode {

    /**
     * Transaction is successfull
     */
    SUCCESS,

    /**
     * Add account failed. Account already exists
     */
    ACCOUNT_ALREADY_EXISTS,

    /**
     * Specified account not found
     */
    ACCOUNT_NOT_FOUND,

    /**
     * Insufficient balance for withdraw
     */
    INSUFFICIENT_BALANCE
}
