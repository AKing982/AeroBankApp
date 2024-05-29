package com.example.aerobankapp.exceptions;

public class AccountNotFoundException extends RuntimeException
{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
