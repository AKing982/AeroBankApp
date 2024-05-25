package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillPaymentDTO(String payeeName,
                             int userID,
                             BigDecimal amount,
                             String payFrom,
                             int acctID,
                             ScheduleFrequency paymentSchedule,
                             boolean enabledReminders,

                             LocalDate paymentDate,
                             LocalDate dueDate,
                             boolean autoPayEnabled) {
}
