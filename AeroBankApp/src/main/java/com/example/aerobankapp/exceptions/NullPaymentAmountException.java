package com.example.aerobankapp.exceptions;

public class NullPaymentAmountException extends RuntimeException
{
    public NullPaymentAmountException(String message) {
        super(message);
    }
}
