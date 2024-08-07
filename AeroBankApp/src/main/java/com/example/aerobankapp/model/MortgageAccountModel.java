package com.example.aerobankapp.model;

import com.example.aerobankapp.account.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MortgageAccountModel implements Account
{

    @Override
    public void deposit(BigDecimal amount) {

    }

    @Override
    public void withdraw(BigDecimal amount) {

    }
}
