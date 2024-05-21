package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record BillPayeeInfoDTO(String payeeName,
                               Date lastPaymentDate,
                               Date nextPaymentDate,
                               BigDecimal paymentAmount) {
}
