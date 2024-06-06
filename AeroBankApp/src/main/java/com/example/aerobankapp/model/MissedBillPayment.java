package com.example.aerobankapp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MissedBillPayment
{
    private BillPayment billPayment;
    private LocalDate originalDueDate;

    public MissedBillPayment(BillPayment billPayment, LocalDate originalDueDate) {
        this.billPayment = billPayment;
        this.originalDueDate = originalDueDate;
    }
}
