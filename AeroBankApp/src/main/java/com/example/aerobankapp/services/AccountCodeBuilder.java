package com.example.aerobankapp.services;

import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.User;

public interface AccountCodeBuilder
{
    AccountCode createAccountCode(User user, Account account);
}
