package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record BillPayeeInfoDTO(String payeeName,
                               LocalDate lastPayment,
                               LocalDate nextPayment,
                               LocalDate paymentDueDate,
                               BigDecimal paymentAmount) {
}
