package com.example.aerobankapp.exceptions;

public class PlaidAccessTokenNotFoundException extends RuntimeException
{
    public PlaidAccessTokenNotFoundException(String message) {
        super(message);
    }
}
