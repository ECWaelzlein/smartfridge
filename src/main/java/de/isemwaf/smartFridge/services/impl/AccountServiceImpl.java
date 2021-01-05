package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Account;
import de.isemwaf.smartFridge.repositories.AccountRepository;
import de.isemwaf.smartFridge.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(long id) {
        return accountRepository.findById(id).isPresent() ? accountRepository.findById(id).get() : null;
    }
}
