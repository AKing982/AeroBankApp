package com.example.aerobankapp.exceptions;

public class StatusNotFoundException extends RuntimeException
{
    public StatusNotFoundException(String message) {
        super(message);
    }
}
