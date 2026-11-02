
package com.bank.bankingaccountservice.service;

import com.bank.bankingaccountservice.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    public Account getAccountByCustomerId(int customerId) {

        if (customerId == 101) {
            return new Account(
                    101,
                    "1234567890",
                    "Savings",
                    new BigDecimal("50000.00")
            );
        }

        return null;
    }
}
