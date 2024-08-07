package com.example.aerobankapp.workbench.utilities.response;

import com.example.aerobankapp.model.AccountNotification;
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
    private int acctID;
    private Long acctCodeID;
    private String shortSegment;
    private BigDecimal balance;
    private BigDecimal pendingAmount;
    private BigDecimal availableAmount;
    private String accountName;
    private String acctColor;
    private String acctImage;
    private AccountNotification accountNotification;

    public AccountResponse(String shortCode, BigDecimal bal, BigDecimal pending, BigDecimal available, String accountName, String acctColor, String acctImage)
    {
        this.shortSegment = shortCode;
        this.balance = bal;
        this.pendingAmount = pending;
        this.availableAmount = available;
        this.accountName = accountName;
        this.acctColor = acctColor;
        this.acctImage = acctImage;
    }

    public AccountResponse(int acctID, Long acctCodeID, String shortCode, BigDecimal balance, BigDecimal pendingAmount, BigDecimal availableAmount, String accountName, String acctColor, String acctImage) {
        this.acctID = acctID;
        this.acctCodeID = acctCodeID;
        this.shortSegment = shortCode;
        this.balance = balance;
        this.pendingAmount = pendingAmount;
        this.availableAmount = availableAmount;
        this.accountName = accountName;
        this.acctColor = acctColor;
        this.acctImage = acctImage;
    }

    public AccountResponse(String shortCode, BigDecimal balance, BigDecimal pendingAmount, BigDecimal availableAmount, String accountName, String acctColor, String acctImage, AccountNotification accountNotification) {
        this.shortSegment = shortCode;
        this.balance = balance;
        this.pendingAmount = pendingAmount;
        this.availableAmount = availableAmount;
        this.accountName = accountName;
        this.acctColor = acctColor;
        this.acctImage = acctImage;
        this.accountNotification = accountNotification;
    }

    public AccountResponse(String accountCode)
    {
        this.shortSegment = accountCode;
    }
}
