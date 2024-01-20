package com.example.aerobankapp.dto;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record WithdrawDTO<T extends AbstractAccountBase>(Long withdrawID,
                                                         int userID,
                                                         String fromAccountID,
                                                         String fromAccountCode,
                                                         T fromAccount,
                                                         BigDecimal amount,
                                                         String description,
                                                         String currency,
                                                         LocalDate posted,
                                                         TransactionStatus status)
{

}
