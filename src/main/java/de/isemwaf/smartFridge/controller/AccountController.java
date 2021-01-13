package de.isemwaf.smartFridge.controller;

import de.isemwaf.smartFridge.model.Account;
import de.isemwaf.smartFridge.model.Fridge;
import de.isemwaf.smartFridge.model.json.AccountModel;
import de.isemwaf.smartFridge.services.AccountService;
import de.isemwaf.smartFridge.services.FridgeService;
import de.isemwaf.smartFridge.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {
    private final AccountService accountService;
    private final FridgeService fridgeService;

    @Autowired
    public AccountController(AccountService accountService, FridgeService fridgeService) {
        this.accountService = accountService;
        this.fridgeService = fridgeService;
    }

    @GetMapping(path = "/api/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount() {
        Account account = accountService.getAccount(Utility.getAccountFromSecurity().getUsername());

        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        account.setPassword("*****");

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping(path = "/api/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountModel accountModel, BindingResult bindingResult) {
        Account account = new Account();
        Fridge fridge = new Fridge();

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        account.setPassword(accountModel.getPassword());
        account.setUsername(accountModel.getUsername());
        account = accountService.createAccount(account);

        fridge.setAccount(account);
        fridge.setInventory(new ArrayList<>(Collections.emptyList()));
        fridgeService.createFridge(fridge);
        account.setPassword("*****");

        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}
