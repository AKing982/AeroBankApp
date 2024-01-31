package com.example.aerobankapp.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BillDTO(Long billId,
                      String accountNumber,
                      String accountCode,
                      String payeeName,
                      String payeeAccountNumber,
                      BigDecimal billAmount,
                      LocalDate dueDate,
                      LocalDate paymentDate,
                      String status,
                      String currency,
                      String referenceNumber,
                      String description,
                      String createdAt,
                      String createdBy,
                      LocalDate createAt,
                      String modifiedBy,
                      LocalDate modifiedAt) {
}
