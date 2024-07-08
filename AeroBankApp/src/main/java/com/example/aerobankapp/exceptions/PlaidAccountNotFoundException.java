package com.example.aerobankapp.exceptions;

public class PlaidAccountNotFoundException extends RuntimeException
{
    public PlaidAccountNotFoundException(String message) {
        super(message);
    }
}
