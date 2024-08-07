package com.example.aerobankapp.exceptions;

public class InvalidLatePaymentException extends IllegalArgumentException
{
    public InvalidLatePaymentException(String s) {
        super(s);
    }
}
