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
    private String accountName;
    private String acctColor;
    private String acctImage;

    public AccountResponse(String acctCode, BigDecimal bal, BigDecimal pending, BigDecimal available, String accountName, String acctColor, String acctImage)
    {
        this.accountCode = acctCode;
        this.balance = bal;
        this.pendingAmount = pending;
        this.availableAmount = available;
        this.accountName = accountName;
        this.acctColor = acctColor;
        this.acctImage = acctImage;
    }

    public AccountResponse(String accountCode)
    {
        this.accountCode = accountCode;
    }
}
