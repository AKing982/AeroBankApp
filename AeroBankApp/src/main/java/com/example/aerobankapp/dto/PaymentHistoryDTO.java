package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentHistoryDTO(String payeeName,
                                BigDecimal paymentAmount,
                                LocalDate postedDate,
                                ScheduleStatus scheduledStatus) {
}
