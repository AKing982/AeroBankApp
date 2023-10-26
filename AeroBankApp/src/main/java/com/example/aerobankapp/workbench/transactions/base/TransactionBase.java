package com.example.aerobankapp.workbench.transactions.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class TransactionBase
{
    protected int userID;
    protected String description;
    protected BigDecimal amount;
    protected LocalDate date_posted;
    protected boolean isDebit;
    protected boolean isProcessed;

    public TransactionBase(String descr, BigDecimal amount, LocalDate date, boolean isDebit)
    {
        this.description = descr;
        this.amount = amount;
        this.date_posted = date;
        this.isDebit = isDebit;
    }
}
