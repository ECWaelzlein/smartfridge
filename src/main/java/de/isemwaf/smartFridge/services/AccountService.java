package de.isemwaf.smartFridge.services;

import de.isemwaf.smartFridge.model.Account;

public interface AccountService {
    Account createAccount(Account account);

    Account getAccount(String username);

    Account getAccountById(Long id);
}
