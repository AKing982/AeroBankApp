package com.example.aerobankapp.model;

import com.example.aerobankapp.scheduler.ScheduleType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Currency;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TransactionCriteria
{
    private String amount;
    private String description;
    private LocalTime scheduledTime;
    private LocalDate scheduledDate;
    private LocalDate posted;
    private ScheduleType scheduleType;
    private Currency currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCriteria that = (TransactionCriteria) o;
        return Objects.equals(amount, that.amount) && Objects.equals(description, that.description) && Objects.equals(scheduledTime, that.scheduledTime) && Objects.equals(scheduledDate, that.scheduledDate) && Objects.equals(posted, that.posted) && scheduleType == that.scheduleType && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, description, scheduledTime, scheduledDate, posted, scheduleType, currency);
    }

    @Override
    public String toString() {
        return "TransactionCriteria{" +
                "amount='" + amount + '\'' +
                ", description='" + description + '\'' +
                ", scheduledTime=" + scheduledTime +
                ", scheduledDate=" + scheduledDate +
                ", posted=" + posted +
                ", scheduleType=" + scheduleType +
                ", currency=" + currency +
                '}';
    }
}
