package com.example.aerobankapp.dto;

import com.example.aerobankapp.workbench.utilities.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionStatementDTO(String description,
                                      BigDecimal debit,
                                      BigDecimal credit,
                                      BigDecimal balance,
                                      LocalDate dateProcessed,
                                      TransactionStatus status)
{

}
