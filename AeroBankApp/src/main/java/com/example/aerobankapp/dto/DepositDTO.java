package com.example.aerobankapp.dto;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.notifications.NotificationType;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder(access = AccessLevel.PUBLIC)
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public final class DepositDTO implements Serializable
{
    private int depositID;
    private int userID;
    private int accountID;
    private String accountCode;
    private BigDecimal amount;
    private LocalDate date;
    private LocalTime timeScheduled;
    private ScheduleType scheduleInterval;
    private String description;
    private NotificationType notificationType;
    private boolean nonUSDCurrency;

    public DepositDTO()
    {

    }
}
