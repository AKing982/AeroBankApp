package com.example.aerobankapp.workbench.utilities.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class AccountResponse implements Serializable
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
