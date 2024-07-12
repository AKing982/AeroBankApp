package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.Withdraw;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WithdrawBalanceSummary extends TransactionBalanceSummary<Withdraw>{

    public WithdrawBalanceSummary(Withdraw transaction, BigDecimal postBalance, LocalDate dateProcessed) {
        super(transaction, postBalance, dateProcessed);
    }

    public WithdrawBalanceSummary() {
    }



    @Override
    protected String generateReportSummary() {
        return null;
    }
}
