package com.example.aerobankapp.exceptions;

public class PlaidTransactionsResponseException extends RuntimeException
{
    public PlaidTransactionsResponseException(String message) {
        super(message);
    }
}
