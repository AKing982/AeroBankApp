package com.example.aerobankapp.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class BillPaymentHistory
{
    private LocalDate nextPaymentDate;
    private LocalDate lastPaymentDate;
    private LocalDate dateUpdated;

    public BillPaymentHistory(LocalDate nextPaymentDate, LocalDate lastPaymentDate, LocalDate dateUpdated) {
        this.nextPaymentDate = nextPaymentDate;
        this.lastPaymentDate = lastPaymentDate;
        this.dateUpdated = dateUpdated;
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