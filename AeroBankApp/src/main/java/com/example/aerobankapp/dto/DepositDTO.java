package com.example.aerobankapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DepositDTO(int depositID,
                         int userID,
                         String accountCode,
                         String amount,
                         String date,
                         String timeScheduled,
                         String scheduleInterval,
                         String description)
{

}
