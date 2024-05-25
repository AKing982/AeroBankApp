package com.example.aerobankapp.exceptions;

public class DuplicateBillPaymentException extends RuntimeException
{
    public DuplicateBillPaymentException(String message) {
        super(message);
    }
}
