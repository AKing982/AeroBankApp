package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class BillPaymentSchedule
{
    private LocalDate dueDate;
    private LocalDate scheduledPaymentDate;
    private ScheduleFrequency scheduleFrequency;
    private ScheduleStatus scheduleStatus;
    private boolean isAutopay;

    public BillPaymentSchedule(LocalDate dueDate, LocalDate scheduledPaymentDate, ScheduleFrequency scheduleFrequency, ScheduleStatus scheduleStatus, boolean isAutopay) {
        this.dueDate = dueDate;
        this.scheduledPaymentDate = scheduledPaymentDate;
        this.scheduleFrequency = scheduleFrequency;
        this.scheduleStatus = scheduleStatus;
        this.isAutopay = isAutopay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillPaymentSchedule that = (BillPaymentSchedule) o;
        return isAutopay == that.isAutopay && Objects.equals(dueDate, that.dueDate) && Objects.equals(scheduledPaymentDate, that.scheduledPaymentDate) && scheduleFrequency == that.scheduleFrequency && scheduleStatus == that.scheduleStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dueDate, scheduledPaymentDate, scheduleFrequency, scheduleStatus, isAutopay);
    }
}
