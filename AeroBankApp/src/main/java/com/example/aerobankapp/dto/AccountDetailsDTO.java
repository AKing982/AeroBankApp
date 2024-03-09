package com.example.aerobankapp.dto;

import java.math.BigDecimal;

public record AccountDetailsDTO(int acctID,
                                String acctCode,
                                String accountName,
                                BigDecimal balance,
                                BigDecimal pending,
                                BigDecimal available) {
}
