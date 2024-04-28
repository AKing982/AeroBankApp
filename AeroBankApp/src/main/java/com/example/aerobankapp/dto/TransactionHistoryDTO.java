package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import com.example.aerobankapp.workbench.utilities.filters.TransactionHistoryFilterType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;

public record TransactionHistoryDTO(EnumSet<TransactionHistoryFilterType> filterType,
                                    String description,
                                    LocalDate startDate,
                                    LocalDate endDate,
                                    TransactionStatus status,
                                    TransactionType transactionType,
                                    LocalTime scheduledTime,
                                    String username,
                                    int accountID,
                                    BigDecimal minAmount,
                                    BigDecimal maxAmount) {
}
