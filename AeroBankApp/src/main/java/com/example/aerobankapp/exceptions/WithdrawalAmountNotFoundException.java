package com.example.aerobankapp.exceptions;

public class WithdrawalAmountNotFoundException extends RuntimeException
{
    public WithdrawalAmountNotFoundException(String message) {
        super(message);
    }
}
