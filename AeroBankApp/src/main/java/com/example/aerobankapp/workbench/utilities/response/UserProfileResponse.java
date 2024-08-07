package com.example.aerobankapp.workbench.utilities.response;

import com.example.aerobankapp.workbench.utilities.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class UserProfileResponse
{
    private int userID;
    private String username;
    private String accountNumber;
    private BigDecimal totalBalance;
    private Long totalAccounts;
    private Role role;

    public UserProfileResponse(String user, String accountNumber, BigDecimal totalBal, Long totalAccounts, Role role)
    {
        this.username = user;
        this.accountNumber = accountNumber;
        this.totalBalance = totalBal;
        this.totalAccounts = totalAccounts;
        this.role = role;
    }

    public UserProfileResponse(int userID, String username, String accountNumber, BigDecimal totalBalance, Long totalAccounts, Role role) {
        this.userID = userID;
        this.username = username;
        this.accountNumber = accountNumber;
        this.totalBalance = totalBalance;
        this.totalAccounts = totalAccounts;
        this.role = role;
    }
}
