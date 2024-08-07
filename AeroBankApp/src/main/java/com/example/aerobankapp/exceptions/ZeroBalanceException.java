package com.example.aerobankapp.exceptions;

public class ZeroBalanceException extends IllegalArgumentException
{
    public ZeroBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZeroBalanceException(String s) {
        super(s);
    }
}
