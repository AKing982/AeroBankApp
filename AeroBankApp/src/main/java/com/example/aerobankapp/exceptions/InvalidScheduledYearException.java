package com.example.aerobankapp.exceptions;

public class InvalidScheduledYearException extends IllegalArgumentException
{
    public InvalidScheduledYearException(String msg)
    {
        super(msg);
    }
}
