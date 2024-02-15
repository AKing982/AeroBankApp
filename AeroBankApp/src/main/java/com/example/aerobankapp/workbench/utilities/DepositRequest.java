package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.scheduler.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest
{
    private int userID;
    private String accountCode;
    private int accountID;
    private String amount;
    private LocalDate date;
    private LocalTime timeScheduled;
    private String scheduleInterval;
    private String description;

    public DepositRequest(int userID, String accountCode, String amount, LocalDate date, LocalTime timeScheduled, String scheduleInterval, String description) {
        this.userID = userID;
        this.accountCode = accountCode;
        this.amount = amount;
        this.date = date;
        this.timeScheduled = timeScheduled;
        this.scheduleInterval = scheduleInterval;
        this.description = description;
    }
}
