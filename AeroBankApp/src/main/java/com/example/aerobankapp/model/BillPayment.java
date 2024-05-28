package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
public class BillPayment
{
    private String payeeName;
    private AccountCode accountCode;
    private BigDecimal paymentAmount;
    private String paymentType;
    private LocalDate dueDate;
    private LocalDate scheduledPaymentDate;
    private ScheduleStatus scheduleStatus;
    private ScheduleFrequency scheduleFrequency;
    private boolean isAutoPayEnabled;

    private LocalDate posted;

    public BillPayment(String payeeName, AccountCode accountCode, BigDecimal paymentAmount, String paymentType, LocalDate dueDate, LocalDate scheduledPaymentDate, ScheduleStatus scheduleStatus, ScheduleFrequency scheduleFrequency, boolean isAutoPayEnabled, LocalDate posted) {
        this.payeeName = payeeName;
        this.accountCode = accountCode;
        this.paymentAmount = paymentAmount;
        this.paymentType = paymentType;
        this.dueDate = dueDate;
        this.scheduledPaymentDate = scheduledPaymentDate;
        this.scheduleStatus = scheduleStatus;
        this.scheduleFrequency = scheduleFrequency;
        this.isAutoPayEnabled = isAutoPayEnabled;
        this.posted = posted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillPayment that = (BillPayment) o;
        return isAutoPayEnabled == that.isAutoPayEnabled && Objects.equals(payeeName, that.payeeName) && Objects.equals(accountCode, that.accountCode) && Objects.equals(paymentAmount, that.paymentAmount) && Objects.equals(paymentType, that.paymentType) && Objects.equals(dueDate, that.dueDate) && Objects.equals(scheduledPaymentDate, that.scheduledPaymentDate) && scheduleStatus == that.scheduleStatus && scheduleFrequency == that.scheduleFrequency && Objects.equals(posted, that.posted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payeeName, accountCode, paymentAmount, paymentType, dueDate, scheduledPaymentDate, scheduleStatus, scheduleFrequency, isAutoPayEnabled, posted);
    }

    public BillPayment(){

    }



}
