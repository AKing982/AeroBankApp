package com.example.aerobankapp.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProcessedBillPayment
{
    private BillPayment billPayment;
    private boolean isComplete;
    private LocalDate lastProcessedDate;

    public ProcessedBillPayment(BillPayment billPayment, boolean isComplete) {
        this.billPayment = billPayment;
        this.isComplete = isComplete;
    }

    public ProcessedBillPayment(BillPayment billPayment, boolean isComplete, LocalDate lastProcessedDate) {
        this.billPayment = billPayment;
        this.isComplete = isComplete;
        this.lastProcessedDate = lastProcessedDate;
    }
}
