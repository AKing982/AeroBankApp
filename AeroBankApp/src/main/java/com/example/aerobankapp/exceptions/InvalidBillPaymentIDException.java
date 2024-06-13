package com.example.aerobankapp.exceptions;

public class InvalidBillPaymentIDException extends RuntimeException
{
    public InvalidBillPaymentIDException(String message) {
        super(message);
    }
}
