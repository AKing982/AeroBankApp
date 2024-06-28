package com.example.aerobankapp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PlaidAccount
{
    private String accountId;
    private String mask;
    private String name;
    private String officialName;
    private String type;
    private String subtype;
    private BigDecimal currentBalance;
    private BigDecimal availableBalance;
    private BigDecimal limit;
    private String verificationStatus;
    private LocalDateTime lastUpdated;

    public PlaidAccount(String accountId, String mask, String name, String officialName, String type, String subtype, BigDecimal currentBalance, BigDecimal availableBalance, BigDecimal limit, String verificationStatus, LocalDateTime lastUpdated) {
        this.accountId = accountId;
        this.mask = mask;
        this.name = name;
        this.officialName = officialName;
        this.type = type;
        this.subtype = subtype;
        this.currentBalance = currentBalance;
        this.availableBalance = availableBalance;
        this.limit = limit;
        this.verificationStatus = verificationStatus;
        this.lastUpdated = lastUpdated;
    }

    public PlaidAccount()
    {

    }
}
