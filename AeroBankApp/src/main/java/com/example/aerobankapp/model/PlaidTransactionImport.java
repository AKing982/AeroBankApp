package com.example.aerobankapp.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PlaidTransactionImport
{
    private String transactionId;
    private int acctID;
    private String transactionName;
    private String referenceNumber;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private boolean isPending;
    private LocalDate posted;

    public PlaidTransactionImport(String transactionId, int acctID, String transactionName, String referenceNumber, BigDecimal amount, LocalDate transactionDate, boolean isPending, LocalDate posted) {
        this.transactionId = transactionId;
        this.acctID = acctID;
        this.transactionName = transactionName;
        this.referenceNumber = referenceNumber;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.isPending = isPending;
        this.posted = posted;
    }


}
