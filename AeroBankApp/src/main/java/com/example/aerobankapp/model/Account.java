package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AccountType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Account
{
    private int accountID;
    private int userID;
    private Long aSecID;
    private Long propsID;
    private String mask;
    private String subType;
    private String type;
    private String user;
    private BigDecimal balance;
    private BigDecimal interest;
    private String accountName;
    private String accountCode;
    private boolean isRentAccount;
    private boolean isMortgageAccount;
    private AccountType accountType;
    private LocalDateTime creationDate;

}
