package com.example.aerobankapp.exceptions;

public class InvalidDepositException extends IllegalArgumentException
{
    public InvalidDepositException(String s) {
        super(s);
    }
}
