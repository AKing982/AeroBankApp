package com.example.aerobankapp.exceptions;

public class InvalidProcessedBillPaymentException extends RuntimeException
{
    public InvalidProcessedBillPaymentException(String message) {
        super(message);
    }
}
