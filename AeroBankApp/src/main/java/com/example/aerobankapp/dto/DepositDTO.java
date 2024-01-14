package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DepositDTO(int depositID,
                         int userID,
                         int acctID,
                         String acctCode,
                         BigDecimal amount,
                         boolean isDebit,
                         boolean isBankTransfer,
                         LocalDate date_posted)
{

}
