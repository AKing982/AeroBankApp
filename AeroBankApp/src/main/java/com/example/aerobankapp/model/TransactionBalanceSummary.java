package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public abstract class TransactionBalanceSummary<T extends TransactionBase>
{
    private T transaction;
    private BigDecimal postBalance;
    private LocalDate dateProcessed;

    public TransactionBalanceSummary(T transaction, BigDecimal postBalance, LocalDate dateProcessed){
        this.postBalance = postBalance;
        this.dateProcessed = dateProcessed;
        this.transaction = transaction;
    }

    public TransactionBalanceSummary(){

    }

    protected abstract String generateReportSummary();


}
