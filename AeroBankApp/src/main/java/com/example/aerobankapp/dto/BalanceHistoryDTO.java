package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceHistoryDTO(int historyID,
                                String acctID,
                                int transactionID,
                                BigDecimal balance,
                                BigDecimal adjusted,
                                BigDecimal lastBalance,
                                LocalDate posted)
{

}
