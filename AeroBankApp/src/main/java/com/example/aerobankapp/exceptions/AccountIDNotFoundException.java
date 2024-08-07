package com.example.aerobankapp.exceptions;

public class AccountIDNotFoundException extends RuntimeException
{
    public AccountIDNotFoundException(String msg)
    {
        super(msg);
    }
}
