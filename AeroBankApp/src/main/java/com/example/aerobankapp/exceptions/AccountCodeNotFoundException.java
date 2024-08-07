package com.example.aerobankapp.exceptions;

public class AccountCodeNotFoundException extends RuntimeException
{
    public AccountCodeNotFoundException(String message) {
        super(message);
    }
}
