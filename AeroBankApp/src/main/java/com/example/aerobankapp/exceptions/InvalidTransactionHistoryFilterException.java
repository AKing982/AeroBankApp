package com.example.aerobankapp.exceptions;

public class InvalidTransactionHistoryFilterException extends RuntimeException
{
    public InvalidTransactionHistoryFilterException(String message) {
        super(message);
    }
}
