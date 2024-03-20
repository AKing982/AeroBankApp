package com.example.aerobankapp.workbench.transactions.base;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Currency;

@Data
@NoArgsConstructor
public abstract class TransactionBase
{
    protected int userID;
    protected String description;
    protected int accountID;
    protected BigDecimal amount;
    protected LocalTime timeScheduled;
    protected ScheduleType scheduleInterval;
    protected LocalDate posted;
    protected LocalDate dateScheduled;
    protected Currency currency;

    public TransactionBase(int userID, String description, int accountID, BigDecimal amount, LocalTime timeScheduled, ScheduleType scheduleInterval, LocalDate date_posted, LocalDate dateScheduled, Currency currency) {
        this.userID = userID;
        this.description = description;
        this.accountID = accountID;
        this.amount = amount;
        this.timeScheduled = timeScheduled;
        this.scheduleInterval = scheduleInterval;
        this.posted = date_posted;
        this.dateScheduled = dateScheduled;
        this.currency = currency;
    }
}
