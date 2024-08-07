package com.example.aerobankapp.dto;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.workbench.utilities.TransferType;
import com.example.aerobankapp.workbench.utilities.User;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record TransferDTO(Long transferID,
                          @Min(1) int fromUserID,
                          @Min(1) int toUserID,
                          @Min(1) int fromAccountID,
                          @Min(1) int toAccountID,
                          @NotNull @DecimalMin("0.00") BigDecimal transferAmount,
                          @NotNull String transferDescription,
                          LocalDate transferDate,
                          LocalTime transferTime,
                          TransferType transferType,
                          boolean notificationEnabled,
                          LocalDate dateTransferred)
{

}
