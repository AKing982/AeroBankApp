package com.example.aerobankapp.dto;

import com.example.aerobankapp.scheduler.ScheduleType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record DepositDTO(int depositID,
                         int userID,
                         String accountCode,
                         BigDecimal amount,
                         LocalDate date,
                         LocalDateTime timeScheduled,
                         ScheduleType scheduleInterval,
                         String description,
                         boolean isProcessed)
{

}
