package com.example.aerobankapp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaidAccount that = (PlaidAccount) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(mask, that.mask) && Objects.equals(name, that.name) && Objects.equals(officialName, that.officialName) && Objects.equals(type, that.type) && Objects.equals(subtype, that.subtype) && Objects.equals(currentBalance, that.currentBalance) && Objects.equals(availableBalance, that.availableBalance) && Objects.equals(limit, that.limit) && Objects.equals(verificationStatus, that.verificationStatus) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, mask, name, officialName, type, subtype, currentBalance, availableBalance, limit, verificationStatus, lastUpdated);
    }
}
