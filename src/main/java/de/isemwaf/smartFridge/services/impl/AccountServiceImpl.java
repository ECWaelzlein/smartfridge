package de.isemwaf.smartFridge.services.impl;

import de.isemwaf.smartFridge.model.Account;
import de.isemwaf.smartFridge.repositories.AccountRepository;
import de.isemwaf.smartFridge.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(long id) {
        return accountRepository.findById(id).isPresent() ? accountRepository.findById(id).get() : null;
    }
}
