package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.utilities.BankAuthorization;

import java.util.concurrent.Callable;

public class AuthorizationRunner implements Callable<BankAuthorization>
{
    private BankAuthorization authorization;

    @Override
    public BankAuthorization call() throws Exception
    {
        return null;
    }
}