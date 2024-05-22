package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillPaymentDTO(String payeeName,
                             BigDecimal amount,
                             LocalDate dueDate,
                             boolean autoPayEnabled) {
}
