package com.example.aerobankapp.workbench.transactions.base;

import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public abstract class TransactionBase
{
    protected int userID;
    protected String description;
    protected String accountID;
    protected BigDecimal amount;
    protected LocalDate date_posted;
    protected TransactionStatus transactionStatus;

    public TransactionBase(int userID, String descr, String acctID, BigDecimal amount, LocalDate posted, TransactionStatus status)
    {
        this.userID = userID;
        this.description = descr;
        this.accountID = acctID;
        this.amount = amount;
        this.date_posted = posted;
        this.transactionStatus = status;
    }

    public abstract void executeTransaction();
    public abstract boolean validateTransaction();
}
