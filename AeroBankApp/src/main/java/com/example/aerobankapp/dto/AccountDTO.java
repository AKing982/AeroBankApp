package com.example.aerobankapp.dto;

import com.example.aerobankapp.account.AccountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;

@Builder
public record AccountDTO(int accountID,
                         String accountCode,
                         int userID,
                         int aSecID,
                         String accountName,
                         BigDecimal balance,
                         BigDecimal interest,
                         AccountType accountType,
                         boolean hasDividend,
                         boolean isRentAccount,
                         boolean hasMortgage,
                         Set<UserDTO> users)
{
}
