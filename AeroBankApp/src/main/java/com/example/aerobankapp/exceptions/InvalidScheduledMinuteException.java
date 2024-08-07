package com.example.aerobankapp.exceptions;

public class InvalidScheduledMinuteException extends IllegalArgumentException
{
    public InvalidScheduledMinuteException(String message)
    {
        super(message);
    }
}
