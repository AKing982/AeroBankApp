package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.account.AccountCodeDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class TransactionSummary
{
    private int transactionID;
    private String accountID;
    private AccountCodeDTO accountCodeDTO;
    private BigDecimal transactionAmount;
    private LocalDate dateCreated;
    private LocalDate datePosted;
    private TransactionType transactionType;
    private String description;
    private BigDecimal balanceAfterTransaction;

}
