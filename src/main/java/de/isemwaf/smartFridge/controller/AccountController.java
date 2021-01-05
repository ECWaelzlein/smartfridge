package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Account;
import de.isemwaf.smartFridge.model.json.AccountModel;
import de.isemwaf.smartFridge.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/api/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@PathVariable String id) {
        Account account = accountService.getAccount(Long.parseLong(id));

        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping(path = "/api/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountModel accountModel, BindingResult bindingResult) {
        Account account = new Account();

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        account.setPassword(accountModel.getPasswordHash());
        account.setUsername(accountModel.getUsername());

        account = accountService.createAccount(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}
