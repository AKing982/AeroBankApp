package com.example.aerobankapp.exceptions;

public class InvalidStringException extends IllegalArgumentException
{
    public InvalidStringException(String s) {
        super(s);
    }
}
