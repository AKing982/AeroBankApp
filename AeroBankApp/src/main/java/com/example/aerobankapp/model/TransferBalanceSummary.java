package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.Transfer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferBalanceSummary extends TransactionBalanceSummary<Transfer>{

    private BigDecimal toAccountPostBalance;

    @Override
    protected String generateReportSummary() {
        return null;
    }
}
