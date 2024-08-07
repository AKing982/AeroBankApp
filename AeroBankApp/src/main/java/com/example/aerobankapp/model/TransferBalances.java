package com.example.aerobankapp.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TransferBalances
{
    private BigDecimal toAccountBalance;
    private BigDecimal fromAccountBalance;

    public TransferBalances(BigDecimal toAccountBal, BigDecimal fromAccountBal){
        this.toAccountBalance = toAccountBal;
        this.fromAccountBalance = fromAccountBal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferBalances that = (TransferBalances) o;
        return Objects.equals(toAccountBalance, that.toAccountBalance) && Objects.equals(fromAccountBalance, that.fromAccountBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toAccountBalance, fromAccountBalance);
    }
}
