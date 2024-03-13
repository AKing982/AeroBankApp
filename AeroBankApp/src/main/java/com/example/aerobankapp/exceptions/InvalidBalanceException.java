package com.example.aerobankapp.exceptions;

public class InvalidBalanceException extends IllegalArgumentException
{
    public InvalidBalanceException(String s) {
        super(s);
    }
}
