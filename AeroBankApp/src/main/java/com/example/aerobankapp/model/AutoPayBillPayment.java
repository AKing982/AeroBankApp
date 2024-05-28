package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class AutoPayBillPayment extends BillPayment
{
    private boolean autoPayEnabled;
    private BillPayment billPayment;

    public AutoPayBillPayment(String payeeName, AccountCode accountCode, BigDecimal paymentAmount, String paymentType, LocalDate dueDate, LocalDate scheduledPaymentDate, ScheduleStatus scheduleStatus, ScheduleFrequency scheduleFrequency, boolean isAutoPayEnabled, LocalDate posted, boolean autoPayEnabled, BillPayment billPayment) {
        super(payeeName, accountCode, paymentAmount, paymentType, dueDate, scheduledPaymentDate, scheduleStatus, scheduleFrequency, isAutoPayEnabled, posted);
        if(isAutoPayEnabled){
            this.autoPayEnabled = true;
        }
        this.billPayment = billPayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AutoPayBillPayment that = (AutoPayBillPayment) o;
        return autoPayEnabled == that.autoPayEnabled && Objects.equals(billPayment, that.billPayment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), autoPayEnabled, billPayment);
    }

    @Override
    public String toString() {
        return "AutoPayBillPayment{" +
                "autoPayEnabled=" + autoPayEnabled +
                ", billPayment=" + billPayment +
                '}';
    }
}
