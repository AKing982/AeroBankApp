package com.example.aerobankapp.exceptions;

public class InvalidBillPaymentException extends RuntimeException
{
    public InvalidBillPaymentException(String message) {
        super(message);
    }
}
