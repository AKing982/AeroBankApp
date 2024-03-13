package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class TransactionStatement
{
    private String description;
    private BigDecimal balance;
    private BigDecimal debit;
    private BigDecimal credit;
    private LocalDate dateProcessed;
    private TransactionStatus transactionStatus;

    public TransactionStatement(String description, BigDecimal balance, BigDecimal debit, BigDecimal credit, LocalDate dateProcessed, TransactionStatus transactionStatus) {
        this.description = description;
        this.balance = balance;
        this.debit = debit;
        this.credit = credit;
        this.dateProcessed = dateProcessed;
        this.transactionStatus = transactionStatus;
    }

    public TransactionStatement(){
        // This isn't the constructor you're looking for
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionStatement that = (TransactionStatement) o;
        return Objects.equals(description, that.description) && Objects.equals(balance, that.balance) && Objects.equals(debit, that.debit) && Objects.equals(credit, that.credit) && Objects.equals(dateProcessed, that.dateProcessed) && transactionStatus == that.transactionStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, balance, debit, credit, dateProcessed, transactionStatus);
    }

    @Override
    public String toString() {
        return "TransactionStatement{" +
                "description='" + description + '\'' +
                ", balance=" + balance +
                ", debit=" + debit +
                ", credit=" + credit +
                ", dateProcessed=" + dateProcessed +
                ", transactionStatus=" + transactionStatus +
                '}';
    }


}
