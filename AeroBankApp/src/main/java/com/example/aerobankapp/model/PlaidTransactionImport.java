package com.example.aerobankapp.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PlaidTransactionImport
{
    private String transactionId;
    private String acctID;
    private String transactionName;
    private String referenceNumber;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private boolean isPending;
    private LocalDate posted;

    public PlaidTransactionImport(String transactionId, String acctID, String transactionName, String referenceNumber, BigDecimal amount, LocalDate transactionDate, boolean isPending, LocalDate posted) {
        this.transactionId = transactionId;
        this.acctID = acctID;
        this.transactionName = transactionName;
        this.referenceNumber = referenceNumber;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.isPending = isPending;
        this.posted = posted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaidTransactionImport that = (PlaidTransactionImport) o;
        return isPending == that.isPending && Objects.equals(transactionId, that.transactionId) && Objects.equals(acctID, that.acctID) && Objects.equals(transactionName, that.transactionName) && Objects.equals(referenceNumber, that.referenceNumber) && Objects.equals(amount, that.amount) && Objects.equals(transactionDate, that.transactionDate) && Objects.equals(posted, that.posted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, acctID, transactionName, referenceNumber, amount, transactionDate, isPending, posted);
    }
}
