package com.example.aerobankapp.exceptions;

public class InvalidUserStringException extends IllegalArgumentException
{
    public InvalidUserStringException(String s) {
        super(s);
    }
}
