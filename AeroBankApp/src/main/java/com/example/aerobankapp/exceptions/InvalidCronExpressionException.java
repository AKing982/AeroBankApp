package com.example.aerobankapp.exceptions;

public class InvalidCronExpressionException extends IllegalArgumentException
{
    public InvalidCronExpressionException(String s)
    {
        super(s);
    }
}
