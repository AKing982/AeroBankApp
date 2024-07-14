package com.example.aerobankapp.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class PlaidTransactionCriteria
{
    private String externalAcctID;
    private String externalId;
    private String description;
    private String merchantName;
    private BigDecimal amount;
    private LocalDate date;
    private LocalDate authorizedDate;
    private boolean pending;

    public PlaidTransactionCriteria(String externalAcctID, String externalId, String description, String merchantName, BigDecimal amount, LocalDate date, LocalDate authorizedDate, boolean pending) {
        this.externalAcctID = externalAcctID;
        this.externalId = externalId;
        this.description = description;
        this.merchantName = merchantName;
        this.amount = amount;
        this.date = date;
        this.authorizedDate = authorizedDate;
        this.pending = pending;
    }

    public PlaidTransactionCriteria(String externalAcctID, String description, String merchantName, BigDecimal amount, LocalDate date, LocalDate authorizedDate, boolean pending) {
        this.externalAcctID = externalAcctID;
        this.description = description;
        this.merchantName = merchantName;
        this.amount = amount;
        this.date = date;
        this.authorizedDate = authorizedDate;
        this.pending = pending;
    }

    public PlaidTransactionCriteria(String description, String merchantName, BigDecimal amount, LocalDate date, LocalDate authorizedDate, boolean pending) {
        this.description = description;
        this.merchantName = merchantName;
        this.amount = amount;
        this.date = date;
        this.authorizedDate = authorizedDate;
        this.pending = pending;
    }
}
