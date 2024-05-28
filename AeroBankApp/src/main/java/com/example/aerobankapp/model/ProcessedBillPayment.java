package com.example.aerobankapp.model;

import lombok.Data;

@Data
public class ProcessedBillPayment
{
    private BillPayment billPayment;
    private boolean isComplete;

    public ProcessedBillPayment(BillPayment billPayment, boolean isComplete) {
        this.billPayment = billPayment;
        this.isComplete = isComplete;
    }
}
