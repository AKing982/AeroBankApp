package com.example.aerobankapp.exceptions;

import org.springframework.security.core.parameters.P;

public class InvalidScheduledIntervalException extends IllegalArgumentException
{
    public InvalidScheduledIntervalException(String msg)
    {
        super(msg);
    }
}
