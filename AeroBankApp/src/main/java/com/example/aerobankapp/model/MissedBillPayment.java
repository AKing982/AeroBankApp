package com.example.aerobankapp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MissedBillPayment
{
    private LocalDate originalDueDate;
    private BigDecimal originalAmount;
    private String payeeName;
    private AccountCode payeeAccountCode;

    public MissedBillPayment(LocalDate originalDueDate, BigDecimal originalAmount, String payeeName, AccountCode payeeAccountCode) {
        this.originalDueDate = originalDueDate;
        this.originalAmount = originalAmount;
        this.payeeName = payeeName;
        this.payeeAccountCode = payeeAccountCode;
    }


}
