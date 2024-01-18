package com.example.aerobankapp.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BalanceHistoryDTO(int historyID,
                                String acctID,
                                int transactionID,
                                BigDecimal balance,
                                BigDecimal adjusted,
                                BigDecimal lastBalance,
                                LocalDate posted)
{

}
