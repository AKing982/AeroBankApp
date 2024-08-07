package com.example.aerobankapp.exceptions;

public class NoWithdrawEntitiesFoundException extends RuntimeException
{
    public NoWithdrawEntitiesFoundException(String message) {
        super(message);
    }
}
