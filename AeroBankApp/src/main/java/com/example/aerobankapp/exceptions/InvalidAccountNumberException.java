package com.example.aerobankapp.exceptions;

public class InvalidAccountNumberException extends IllegalArgumentException
{
    public InvalidAccountNumberException(String s) {
        super(s);
    }
}
