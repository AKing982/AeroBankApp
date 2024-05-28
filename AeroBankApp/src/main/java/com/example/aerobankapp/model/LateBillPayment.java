package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
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

    public LateBillPayment(String payeeName, AccountCode accountCode, BigDecimal paymentAmount, String paymentType, LocalDate dueDate, LocalDate scheduledPaymentDate, ScheduleStatus scheduleStatus, ScheduleFrequency scheduleFrequency, boolean isAutoPayEnabled, LocalDate posted, LocalDate originalDueDate, LocalDate missedDate, BigDecimal lateFee) {
        super(payeeName, accountCode, paymentAmount, paymentType, dueDate, scheduledPaymentDate, scheduleStatus, scheduleFrequency, isAutoPayEnabled, posted);
        this.originalDueDate = originalDueDate;
        this.missedDate = missedDate;
        this.lateFee = lateFee;
    }
}
