package com.example.aerobankapp.workbench.generator;

import com.example.aerobankapp.model.AccountNumber;

public interface AccountNumberGenerator
{
    AccountNumber generateAccountNumber(String user);
}
