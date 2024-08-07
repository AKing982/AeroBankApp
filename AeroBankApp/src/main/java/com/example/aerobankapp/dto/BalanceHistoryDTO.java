package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;

@Builder
public record BalanceHistoryDTO(int historyID,
                                String acctID,
                                int transactionID,
                                BigDecimal balance,
                                BigDecimal adjusted,
                                BigDecimal lastBalance,
                                TransactionType type,
                                String createdBy,
                                LocalDateTime createdAt,
                                String updatedBy,
                                Currency currency,
                                LocalDate posted)
{

}
