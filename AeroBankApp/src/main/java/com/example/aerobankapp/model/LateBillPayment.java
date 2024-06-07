package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class LateBillPayment
{
    private LocalDate originalDueDate;

    private BigDecimal lateFee;

    private BillPayment billPayment;

    public LateBillPayment(LocalDate originalDueDate, BigDecimal lateFee, BillPayment billPayment) {
       this.originalDueDate = originalDueDate;
       this.lateFee = lateFee;
       this.billPayment = billPayment;
    }

    public LateBillPayment(){

    }
}
