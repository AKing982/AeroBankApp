package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchCriteria
{
    private int accountId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private TransactionType transactionType;
    private ReferenceNumber referenceNumber;
    private boolean isPending;

    public SearchCriteria(int accountId, LocalDate startDate, LocalDate endDate, String description, BigDecimal minAmount, BigDecimal maxAmount, TransactionType transactionType, ReferenceNumber referenceNumber, boolean isPending) {
        this.accountId = accountId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.transactionType = transactionType;
        this.referenceNumber = referenceNumber;
        this.isPending = isPending;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "accountId=" + accountId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                ", transactionType=" + transactionType +
                ", referenceNumber=" + referenceNumber +
                ", isPending=" + isPending +
                '}';
    }
}
