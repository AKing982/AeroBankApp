package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DepositDTO(int depositID,
                         int userID,
                         String accountID,
                         BigDecimal amount,
                         LocalDate date,
                         String description)
{

}
