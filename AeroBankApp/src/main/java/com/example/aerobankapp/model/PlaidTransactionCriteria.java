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
    private String description;
    private BigDecimal amount;
    private BigDecimal balance;
    private LocalDate date;

    public PlaidTransactionCriteria(String description, BigDecimal amount, BigDecimal balance, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.balance = balance;
        this.date = date;
    }


}
