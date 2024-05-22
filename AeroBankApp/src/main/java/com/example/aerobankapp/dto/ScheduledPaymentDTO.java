package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ScheduledPaymentDTO(String payeeName,
                                  String lastPayment,
                                  String nextPayment,
                                  LocalDate dueDate,
                                  String paymentAmount) {
}
