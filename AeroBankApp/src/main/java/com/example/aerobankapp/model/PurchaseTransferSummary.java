package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.Purchase;

public class PurchaseTransferSummary extends TransactionBalanceSummary<Purchase> {


    @Override
    protected String generateReportSummary() {
        return null;
    }
}
