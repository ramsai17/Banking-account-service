
package com.bank.bankingaccountservice.controller;

import com.bank.bankingaccountservice.model.Account;
import com.bank.bankingaccountservice.service.AccountService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Account> getAccount(
            @PathVariable int customerId) {

        Account account =
                accountService.getAccountByCustomerId(customerId);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(account);
    }
}
