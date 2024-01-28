package com.example.aerobankapp.workbench.utilities.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class UserProfileResponse
{
    private String username;
    private String accountNumber;
    private BigDecimal totalBalance;
    private Long totalAccounts;

    public UserProfileResponse(String user, String accountNumber, BigDecimal totalBal, Long totalAccounts)
    {
        this.username = user;
        this.accountNumber = accountNumber;
        this.totalBalance = totalBal;
        this.totalAccounts = totalAccounts;
    }
}
