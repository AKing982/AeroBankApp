package com.example.aerobankapp.model;

import com.example.aerobankapp.exceptions.AutoPayBillPaymentCreationException;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class AutoPayBillPayment extends BillPayment
{
    private boolean autoPayEnabled;

    public AutoPayBillPayment(String payeeName, AccountCode accountCode, BigDecimal paymentAmount, String paymentType, LocalDate dueDate, LocalDate scheduledPaymentDate, ScheduleStatus scheduleStatus, ScheduleFrequency scheduleFrequency, boolean isAutoPayEnabled, LocalDate posted) {
        super(payeeName, accountCode, paymentAmount, paymentType, dueDate, scheduledPaymentDate, scheduleStatus, scheduleFrequency, isAutoPayEnabled, posted);
        if (isAutoPayEnabled) {
            this.autoPayEnabled = true;
        }else{
            throw new AutoPayBillPaymentCreationException("Unable to create AutoPayBillPayment object due to autoPay not enabled.");
        }
    }
}
