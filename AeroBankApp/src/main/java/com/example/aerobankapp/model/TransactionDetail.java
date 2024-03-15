package com.example.aerobankapp.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Deprecated
public class TransactionDetail
{
    private Long transactionDetailID;
    private int userID;
    private String user;
    private int accountID;
    private String accountCode;
    private int referenceAcctID;
    private int transactionID;
    private BigDecimal balance;

}
