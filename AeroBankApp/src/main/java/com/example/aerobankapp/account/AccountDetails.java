package com.example.aerobankapp.account;

import com.example.aerobankapp.model.Balance;
import com.example.aerobankapp.model.User;

import java.math.BigDecimal;

public class AccountDetails implements Balance
{
    private User user;
    private BigDecimal balance;

    public AccountDetails(User user, BigDecimal balance)
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
