package com.example.aerobankapp.workbench.generator;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.workbench.model.AccountNumber;
import com.example.aerobankapp.workbench.utilities.UserType;

public class AccountNumberGenerator implements Generator<AccountNumber>
{
    private User user;
    private UserType userType;
    private AccountType accountType;

    @Override
    public AccountNumber generate()
    {
        AccountNumber accountNumber = null;
        return accountNumber;

    }
}
