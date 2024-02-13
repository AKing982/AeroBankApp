package com.example.aerobankapp.dto;

import com.example.aerobankapp.scheduler.ScheduleType;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder(access = AccessLevel.PUBLIC)
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public final class DepositDTO
{
    private int depositID;
    private int userID;
    private int accountID;
    private String accountCode;
    private BigDecimal amount;
    private LocalDate date;
    private LocalDateTime timeScheduled;
    private ScheduleType scheduleInterval;
    private String description;
    private boolean nonUSDCurrency;
}
