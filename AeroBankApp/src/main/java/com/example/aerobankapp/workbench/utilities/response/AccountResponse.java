package com.example.aerobankapp.workbench.utilities.response;

import java.math.BigDecimal;

public class AccountResponse
{
    private String accountCode;
    private BigDecimal balance;
    private BigDecimal pendingAmount;
    private BigDecimal availableAmount;

    public AccountResponse(String acctCode, BigDecimal bal, BigDecimal pending, BigDecimal available)
    {
        this.accountCode = acctCode;
        this.balance = bal;
        this.pendingAmount = pending;
        this.availableAmount = available;
    }
}
