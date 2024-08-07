package com.example.aerobankapp.dto;

import com.example.aerobankapp.account.AccountType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountInfoDTO(String accountName, String accountType, BigDecimal initialBalance) {
}
