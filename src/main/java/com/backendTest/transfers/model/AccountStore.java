package com.backendTest.transfers.model;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Optional.ofNullable;

/**
 * Store for {@link Account}s
 */
public class AccountStore {

    /**
     * Map of id to {@link Account}
     */
    private final ConcurrentMap<Long, Account> accounts = new ConcurrentHashMap<>();

    /**
     * Add account with specifid id
     * @param id is the id
     * @return true if added successfully, false if already present
     */
    public boolean addAccount(long id) {
        Account account = new Account(id);
        Account prev = accounts.putIfAbsent(id, account);
        return prev == null;
    }

    /**
     * Get account with id
     * @param id is the id
     * @return Account if present, otherwise {@link Optional#empty()}
     */
    public Optional<Account> getAccount(long id) {
        return ofNullable(accounts.get(id));
    }

}
