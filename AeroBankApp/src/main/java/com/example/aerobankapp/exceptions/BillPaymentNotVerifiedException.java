package com.example.aerobankapp.exceptions;

public class BillPaymentNotVerifiedException extends RuntimeException
{
    public BillPaymentNotVerifiedException(String message) {
        super(message);
    }
}
