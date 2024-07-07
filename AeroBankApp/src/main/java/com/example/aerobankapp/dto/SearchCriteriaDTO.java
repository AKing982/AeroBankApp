package com.example.aerobankapp.dto;

import com.example.aerobankapp.model.ReferenceNumber;
import com.example.aerobankapp.workbench.transactions.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SearchCriteriaDTO(int accountId,
                                LocalDate startDate,
                                LocalDate endDate,
                                String description,
                                BigDecimal minAmount,
                                BigDecimal maxAmount,
                                TransactionType transactionType,
                                ReferenceNumber referenceNumber,
                                boolean isPending) {
}
