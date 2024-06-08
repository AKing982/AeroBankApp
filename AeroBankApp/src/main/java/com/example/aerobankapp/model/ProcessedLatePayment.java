package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProcessedLatePayment
{
    private LateBillPayment lateBillPayment;
    private ConfirmationNumber confirmationNumber;
    private ReferenceNumber referenceNumber;
    private TransactionStatus transactionStatus;
    private BigDecimal finalLateFee;
    private LocalDate dateProcessed;

    public ProcessedLatePayment(LateBillPayment lateBillPayment, ConfirmationNumber confirmationNumber, ReferenceNumber referenceNumber, TransactionStatus transactionStatus, BigDecimal finalLateFee, LocalDate dateProcessed) {
        this.lateBillPayment = lateBillPayment;
        this.confirmationNumber = confirmationNumber;
        this.referenceNumber = referenceNumber;
        this.transactionStatus = transactionStatus;
        this.finalLateFee = finalLateFee;
        this.dateProcessed = dateProcessed;
    }
}
