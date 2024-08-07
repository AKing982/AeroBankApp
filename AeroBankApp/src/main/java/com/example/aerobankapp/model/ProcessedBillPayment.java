package com.example.aerobankapp.model;

import lombok.Data;

import java.time.LocalDate;


public class ProcessedBillPayment
{
    private BillPayment billPayment;
    private boolean isComplete;
    private LocalDate lastProcessedDate;
    private LocalDate nextPaymentDate;

    public ProcessedBillPayment(BillPayment billPayment, boolean isComplete) {
        this.billPayment = billPayment;
        this.isComplete = isComplete;
    }

    public ProcessedBillPayment(){

    }

    public ProcessedBillPayment(BillPayment billPayment, boolean isComplete, LocalDate lastProcessedDate) {
        this.billPayment = billPayment;
        this.isComplete = isComplete;
        this.lastProcessedDate = lastProcessedDate;
    }

    public ProcessedBillPayment(BillPayment billPayment, boolean isComplete, LocalDate lastProcessedDate, LocalDate nextPaymentDate) {
        this.billPayment = billPayment;
        this.isComplete = isComplete;
        this.lastProcessedDate = lastProcessedDate;
        this.nextPaymentDate = nextPaymentDate;
    }

    public BillPayment getBillPayment() {
        return billPayment;
    }

    public void setBillPayment(BillPayment billPayment) {
        this.billPayment = billPayment;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public LocalDate getLastProcessedDate() {
        return lastProcessedDate;
    }

    public void setLastProcessedDate(LocalDate lastProcessedDate) {
        this.lastProcessedDate = lastProcessedDate;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }
}
