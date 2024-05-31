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
    private Long paymentID;
    private int userID;
    private String payeeName;
    private AccountCode accountCode;
    private BigDecimal paymentAmount;
    private String paymentType;
    private LocalDate dueDate;
    private LocalDate scheduledPaymentDate;
    private ScheduleStatus scheduleStatus;
    private ScheduleFrequency scheduleFrequency;
    private Long schedulePaymentID;
    private boolean isAutoPayEnabled;
    private boolean isProcessed;
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

    public BillPayment(Long paymentID, String payeeName, AccountCode accountCode, BigDecimal paymentAmount, String paymentType, LocalDate dueDate, LocalDate scheduledPaymentDate, ScheduleStatus scheduleStatus, ScheduleFrequency scheduleFrequency, boolean isAutoPayEnabled, LocalDate posted) {
        this.paymentID = paymentID;
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

    public BillPayment(Long paymentID, int userID, String payeeName, AccountCode accountCode, BigDecimal paymentAmount, String paymentType, LocalDate dueDate, LocalDate scheduledPaymentDate, ScheduleStatus scheduleStatus, ScheduleFrequency scheduleFrequency, Long schedulePaymentID, boolean isAutoPayEnabled, boolean isProcessed, LocalDate posted) {
        this.paymentID = paymentID;
        this.userID = userID;
        this.payeeName = payeeName;
        this.accountCode = accountCode;
        this.paymentAmount = paymentAmount;
        this.paymentType = paymentType;
        this.dueDate = dueDate;
        this.scheduledPaymentDate = scheduledPaymentDate;
        this.scheduleStatus = scheduleStatus;
        this.scheduleFrequency = scheduleFrequency;
        this.schedulePaymentID = schedulePaymentID;
        this.isAutoPayEnabled = isAutoPayEnabled;
        this.isProcessed = isProcessed;
        this.posted = posted;
    }

    public BillPayment(){

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillPayment that = (BillPayment) o;
        return isAutoPayEnabled == that.isAutoPayEnabled && Objects.equals(paymentID, that.paymentID) && Objects.equals(payeeName, that.payeeName) && Objects.equals(accountCode, that.accountCode) && Objects.equals(paymentAmount, that.paymentAmount) && Objects.equals(paymentType, that.paymentType) && Objects.equals(dueDate, that.dueDate) && Objects.equals(scheduledPaymentDate, that.scheduledPaymentDate) && scheduleStatus == that.scheduleStatus && scheduleFrequency == that.scheduleFrequency && Objects.equals(posted, that.posted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentID, payeeName, accountCode, paymentAmount, paymentType, dueDate, scheduledPaymentDate, scheduleStatus, scheduleFrequency, isAutoPayEnabled, posted);
    }
}
