package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor(access= AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Transaction
{
    private int transactionID;
    private int historyID;
    private int referenceID;
    private int acctID;
    private BigDecimal amount;
    private String description;
    private LocalDate posted;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionID == that.transactionID && historyID == that.historyID && referenceID == that.referenceID && acctID == that.acctID && Objects.equals(amount, that.amount) && Objects.equals(description, that.description) && Objects.equals(posted, that.posted) && transactionType == that.transactionType && transactionStatus == that.transactionStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionID, historyID, referenceID, acctID, amount, description, posted, transactionType, transactionStatus);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", historyID=" + historyID +
                ", referenceID=" + referenceID +
                ", acctID=" + acctID +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", posted=" + posted +
                ", transactionType=" + transactionType +
                ", transactionStatus=" + transactionStatus +
                '}';
    }
}
