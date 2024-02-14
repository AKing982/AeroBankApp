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
    private String amount;
    private String description;
    private String interval;
    private LocalTime selectedTime;
    private LocalDate scheduledDate;
}
