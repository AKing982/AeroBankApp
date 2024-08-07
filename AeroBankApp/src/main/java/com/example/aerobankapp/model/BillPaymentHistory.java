package com.example.aerobankapp.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;


public class BillPaymentHistory
{
    private Long paymentId;
    private LocalDate nextPaymentDate;
    private LocalDate lastPaymentDate;
    private LocalDate dateUpdated;
    private boolean isProcessed;

    public BillPaymentHistory(LocalDate nextPaymentDate, LocalDate lastPaymentDate, LocalDate dateUpdated) {
        this.nextPaymentDate = nextPaymentDate;
        this.lastPaymentDate = lastPaymentDate;
        this.dateUpdated = dateUpdated;
    }

    public BillPaymentHistory(LocalDate nextPaymentDate, LocalDate lastPaymentDate, LocalDate dateUpdated, boolean isProcessed) {
        this.nextPaymentDate = nextPaymentDate;
        this.lastPaymentDate = lastPaymentDate;
        this.dateUpdated = dateUpdated;
        this.isProcessed = isProcessed;
    }

    public BillPaymentHistory(Long paymentId, LocalDate nextPaymentDate, LocalDate lastPaymentDate, LocalDate dateUpdated, boolean isProcessed) {
        this.paymentId = paymentId;
        this.nextPaymentDate = nextPaymentDate;
        this.lastPaymentDate = lastPaymentDate;
        this.dateUpdated = dateUpdated;
        this.isProcessed = isProcessed;
    }

    public BillPaymentHistory(){

    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LocalDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public LocalDate getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDate dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillPaymentHistory that = (BillPaymentHistory) o;
        return Objects.equals(nextPaymentDate, that.nextPaymentDate) && Objects.equals(lastPaymentDate, that.lastPaymentDate) && Objects.equals(dateUpdated, that.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextPaymentDate, lastPaymentDate, dateUpdated);
    }
}
