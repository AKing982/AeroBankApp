package com.example.aerobankapp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class AccountDetails
{
    private int acctID;
    private BigDecimal available;
    private BigDecimal pending;
    private BigDecimal balance;
    private BigDecimal currentFees;

    public AccountDetails(int acctID, BigDecimal available, BigDecimal pending, BigDecimal balance, BigDecimal currentFees) {
        this.acctID = acctID;
        this.available = available;
        this.pending = pending;
        this.balance = balance;
        this.currentFees = currentFees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDetails that = (AccountDetails) o;
        return acctID == that.acctID && Objects.equals(available, that.available) && Objects.equals(pending, that.pending) && Objects.equals(balance, that.balance) && Objects.equals(currentFees, that.currentFees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acctID, available, pending, balance, currentFees);
    }
}
