package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class BalanceHistory
{
    private Long historyID;
    private int accountID;
    private int toAccountID;
    private int fromAccountID;
    private BigDecimal currentBalance;
    private BigDecimal adjustedAmount;
    private BigDecimal newBalance;
    private TransactionType transactionType;
    private String createdBy;
    private String updatedBy;
    private LocalDate posted;
}
