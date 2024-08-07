package com.example.aerobankapp.exceptions;

public class InvalidAccountIDException extends IllegalArgumentException
{
    public InvalidAccountIDException(String s) {
        super(s);
    }

    public InvalidAccountIDException(String message, Throwable cause) {
        super(message, cause);
    }
}
