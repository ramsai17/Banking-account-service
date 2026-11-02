
package com.bank.bankingaccountservice.model;

import java.math.BigDecimal;

public class Account {

    private int customerId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;

    public Account(int customerId, String accountNumber,
                   String accountType, BigDecimal balance) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
