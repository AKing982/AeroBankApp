package com.example.aerobankapp.workbench.utilities.response;

import com.example.aerobankapp.scheduler.ScheduleType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class DepositResponse
{
    private int userID;
    private String accountCode;
    private int accountID;
    private BigDecimal amount;
    private String description;
    private ScheduleType interval;
    private LocalTime selectedTime;
    private LocalDate scheduledDate;

    public DepositResponse(int userID, String accountCode, BigDecimal amount, String description, ScheduleType interval, LocalTime selectedTime, LocalDate scheduledDate) {
        this.userID = userID;
        this.accountCode = accountCode;
        this.amount = amount;
        this.description = description;
        this.interval = interval;
        this.selectedTime = selectedTime;
        this.scheduledDate = scheduledDate;
    }

    public DepositResponse(int userID, String accountCode, int accountID, BigDecimal amount, String description, ScheduleType interval, LocalTime selectedTime, LocalDate scheduledDate) {
        this.userID = userID;
        this.accountCode = accountCode;
        this.accountID = accountID;
        this.amount = amount;
        this.description = description;
        this.interval = interval;
        this.selectedTime = selectedTime;
        this.scheduledDate = scheduledDate;
    }
}
