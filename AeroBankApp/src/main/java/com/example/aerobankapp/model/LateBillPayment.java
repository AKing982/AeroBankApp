package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;



public class LateBillPayment
{
    private LocalDate originalDueDate;

    private BigDecimal lateFee;

    private int daysLate;

    private BillPayment billPayment;

    public LateBillPayment(LocalDate originalDueDate, BigDecimal lateFee, BillPayment billPayment) {
       this.originalDueDate = originalDueDate;
       this.lateFee = lateFee;
       this.billPayment = billPayment;
    }

    public LateBillPayment(LocalDate originalDueDate, BigDecimal lateFee, int daysLate, BillPayment billPayment) {
        this.originalDueDate = originalDueDate;
        this.lateFee = lateFee;
        this.daysLate = daysLate;
        this.billPayment = billPayment;
    }

    public LateBillPayment(){

    }

    public LocalDate getOriginalDueDate() {
        return originalDueDate;
    }

    public void setOriginalDueDate(LocalDate originalDueDate) {
        this.originalDueDate = originalDueDate;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public int getDaysLate() {
        return daysLate;
    }

    public void setDaysLate(int daysLate) {
        this.daysLate = daysLate;
    }

    public BillPayment getBillPayment() {
        return billPayment;
    }

    public void setBillPayment(BillPayment billPayment) {
        this.billPayment = billPayment;
    }
}
