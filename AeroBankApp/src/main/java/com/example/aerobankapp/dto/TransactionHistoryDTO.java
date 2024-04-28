package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import com.example.aerobankapp.workbench.utilities.filters.TransactionHistoryFilterType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;

public record TransactionHistoryDTO( @JsonProperty("filterType") List<String> filterType,
                                     @JsonProperty("description") String description,
                                     @JsonProperty("startDate") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate startDate,
                                     @JsonProperty("endDate") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate endDate,
                                     @JsonProperty("status") TransactionStatus status,
                                     @JsonProperty("transactionType") TransactionType transactionType,
                                     @JsonProperty("scheduledTime") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss") LocalTime scheduledTime,
                                     @JsonProperty("username") String username,
                                     @JsonProperty("accountID") int accountID,
                                     @JsonProperty("minAmount") BigDecimal minAmount,
                                     @JsonProperty("maxAmount") BigDecimal maxAmount) {
}
