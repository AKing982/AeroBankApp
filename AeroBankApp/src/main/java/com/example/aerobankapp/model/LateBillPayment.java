package com.example.aerobankapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class LateBillPayment extends BillPayment
{
    private LocalDate originalDueDate;

    private LocalDate missedDate;

    private BigDecimal lateFee;

    public LateBillPayment(LocalDate dueDate, LocalDate missedDate, BigDecimal lateFee, String payeeName, AccountCode accountCode, BigDecimal paymentAmount, String paymentType, LocalDate posted) {
        super(payeeName, accountCode, paymentAmount, paymentType, posted);
        this.originalDueDate = dueDate;
        this.missedDate = missedDate;
        this.lateFee = lateFee;

    }

}
