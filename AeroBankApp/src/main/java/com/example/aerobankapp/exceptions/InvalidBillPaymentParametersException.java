package com.example.aerobankapp.exceptions;

public class InvalidBillPaymentParametersException extends RuntimeException
{
    public InvalidBillPaymentParametersException(String message) {
        super(message);
    }
}
