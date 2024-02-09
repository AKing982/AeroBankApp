package com.example.aerobankapp.exceptions;

public class InvalidScheduledHourException extends IllegalArgumentException
{
    public InvalidScheduledHourException(String msg)
    {
        super(msg);
    }
}
