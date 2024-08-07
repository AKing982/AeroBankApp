package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseDTO(Long purchaseID,
                          int userID,
                          String description,
                          BigDecimal amount,
                          String accountID,
                          String accountCode,
                          LocalDate posted,
                          boolean isProcessed)
{
}
