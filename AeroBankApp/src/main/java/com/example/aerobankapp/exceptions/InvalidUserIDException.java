package com.example.aerobankapp.exceptions;

public class InvalidUserIDException extends IllegalArgumentException
{
    public InvalidUserIDException() {
    }

    public InvalidUserIDException(String s) {
        super(s);
    }
}
