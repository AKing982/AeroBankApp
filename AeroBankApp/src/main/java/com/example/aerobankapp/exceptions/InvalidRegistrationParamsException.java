package com.example.aerobankapp.exceptions;

public class InvalidRegistrationParamsException extends RuntimeException
{
    public InvalidRegistrationParamsException(String message) {
        super(message);
    }
}
