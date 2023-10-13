package com.example.aerobankapp.account;

import com.example.aerobankapp.model.Balance;
import com.example.aerobankapp.model.UserDTO;

import java.math.BigDecimal;

public class AccountDetails implements Balance
{
    private UserDTO user;
    private BigDecimal balance;

    public AccountDetails(UserDTO user, BigDecimal balance)
    {
        this.user = user;
        this.balance = balance;
    }

    @Override
    public BigDecimal getBalance()
    {
        return balance;
    }
}
