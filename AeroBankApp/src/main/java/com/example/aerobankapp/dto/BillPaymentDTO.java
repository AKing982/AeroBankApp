package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillPaymentDTO(String payeeName,
                             int userID,
                             BigDecimal amount,
                             String payFrom,
                             int acctID,
                             LocalDate dueDate,
                             boolean autoPayEnabled) {
}
