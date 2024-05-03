package com.example.aerobankapp.workbench.transactionHistory.criteria;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoryCriteria(String description, BigDecimal minAmount,
                              BigDecimal maxAmount, LocalDate startDate, LocalDate endDate,
                              TransactionStatus status, TransactionType transactionType)
{

}
