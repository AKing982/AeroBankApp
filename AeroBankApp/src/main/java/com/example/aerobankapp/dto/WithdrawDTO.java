package com.example.aerobankapp.dto;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record WithdrawDTO(Long withdrawID,
                          int userID,
                          Account fromAccount,
                          BigDecimal amount,
                          String description,
                          String currency,
                          LocalDate posted,
                          TransactionStatus status)
{

}
