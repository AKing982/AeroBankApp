package com.example.aerobankapp.workbench.transactionHistory.criteria;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import com.example.aerobankapp.workbench.utilities.TransferType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record HistoryCriteria(String description, BigDecimal minAmount,
                              BigDecimal maxAmount, LocalDate startDate, LocalDate endDate,
                              LocalTime scheduledTime, TransferType transferType,
                              TransactionStatus status, TransactionType transactionType)
{

}
