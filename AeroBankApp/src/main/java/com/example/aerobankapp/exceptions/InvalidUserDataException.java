package com.example.aerobankapp.exceptions;

public class InvalidUserDataException extends RuntimeException
{
    public InvalidUserDataException(String message) {
        super(message);
    }
}
