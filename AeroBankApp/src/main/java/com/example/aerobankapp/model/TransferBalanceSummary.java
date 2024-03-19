package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.Transfer;

public class TransferBalanceSummary extends TransactionBalanceSummary<Transfer>{

    @Override
    protected String generateReportSummary() {
        return null;
    }
}
