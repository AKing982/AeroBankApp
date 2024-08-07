package com.example.aerobankapp.exceptions;

public class PlaidApiResponseException extends RuntimeException
{
    public PlaidApiResponseException(String message) {
        super(message);
    }
}
