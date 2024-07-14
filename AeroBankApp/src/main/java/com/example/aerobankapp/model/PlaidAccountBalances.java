package com.example.aerobankapp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class PlaidAccountBalances
{
    private int userId;
    private String accountId;
    private BigDecimal currentBalance;
    private BigDecimal availableBalance;
    private BigDecimal pendingBalance;

    public PlaidAccountBalances(String accountId, BigDecimal currentBalance, BigDecimal availableBalance, BigDecimal pendingBalance) {
        this.accountId = accountId;
        this.currentBalance = currentBalance;
        this.availableBalance = availableBalance;
        this.pendingBalance = pendingBalance;
    }

    public PlaidAccountBalances(int userId, String accountId, BigDecimal currentBalance, BigDecimal availableBalance, BigDecimal pendingBalance) {
        this.userId = userId;
        this.accountId = accountId;
        this.currentBalance = currentBalance;
        this.availableBalance = availableBalance;
        this.pendingBalance = pendingBalance;
    }

    public PlaidAccountBalances()
    {
        // Empty Constructor
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaidAccountBalances that = (PlaidAccountBalances) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(currentBalance, that.currentBalance) && Objects.equals(availableBalance, that.availableBalance) && Objects.equals(pendingBalance, that.pendingBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, currentBalance, availableBalance, pendingBalance);
    }

    @Override
    public String toString() {
        return "PlaidAccountBalances{" +
                "accountId='" + accountId + '\'' +
                ", currentBalance=" + currentBalance +
                ", availableBalance=" + availableBalance +
                ", pendingBalance=" + pendingBalance +
                '}';
    }
}
