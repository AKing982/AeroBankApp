package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;


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

    public Long getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Long paymentID) {
        this.paymentID = paymentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public AccountCode getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(AccountCode accountCode) {
        this.accountCode = accountCode;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getScheduledPaymentDate() {
        return scheduledPaymentDate;
    }

    public void setScheduledPaymentDate(LocalDate scheduledPaymentDate) {
        this.scheduledPaymentDate = scheduledPaymentDate;
    }

    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public ScheduleFrequency getScheduleFrequency() {
        return scheduleFrequency;
    }

    public void setScheduleFrequency(ScheduleFrequency scheduleFrequency) {
        this.scheduleFrequency = scheduleFrequency;
    }

    public Long getSchedulePaymentID() {
        return schedulePaymentID;
    }

    public void setSchedulePaymentID(Long schedulePaymentID) {
        this.schedulePaymentID = schedulePaymentID;
    }

    public boolean isAutoPayEnabled() {
        return isAutoPayEnabled;
    }

    public void setAutoPayEnabled(boolean autoPayEnabled) {
        isAutoPayEnabled = autoPayEnabled;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public LocalDate getPosted() {
        return posted;
    }

    public void setPosted(LocalDate posted) {
        this.posted = posted;
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
