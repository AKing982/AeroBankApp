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
    private String amount;
    private String description;
    private String interval;
    private LocalTime selectedTime;
    private LocalDate scheduledDate;

    public DepositResponse(int userID, String accountCode, String amount, String description, String interval, LocalTime selectedTime, LocalDate scheduledDate) {
        this.userID = userID;
        this.accountCode = accountCode;
        this.amount = amount;
        this.description = description;
        this.interval = interval;
        this.selectedTime = selectedTime;
        this.scheduledDate = scheduledDate;
    }

    public DepositResponse(int userID, String accountCode, int accountID, String amount, String description, String interval, LocalTime selectedTime, LocalDate scheduledDate) {
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
