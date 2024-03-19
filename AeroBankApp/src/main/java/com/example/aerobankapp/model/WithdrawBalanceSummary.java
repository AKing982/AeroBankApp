package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.Withdraw;

public class WithdrawBalanceSummary extends TransactionBalanceSummary<Withdraw>{
    @Override
    protected String generateReportSummary() {
        return null;
    }
}
