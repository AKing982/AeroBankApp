package com.example.aerobankapp.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Deprecated
public record ProcessedDepositDTO(int depositID,
                                int userID,
                                int accountID,
                                String accountCode,
                                BigDecimal newBalance,
                                BigDecimal amount,
                                String description,
                                LocalDateTime createdAt,
                                String createdBy) {
}
