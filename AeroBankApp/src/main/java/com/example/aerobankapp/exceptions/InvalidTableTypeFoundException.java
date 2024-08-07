package com.example.aerobankapp.exceptions;

public class InvalidTableTypeFoundException extends RuntimeException
{
    public InvalidTableTypeFoundException(String message) {
        super(message);
    }
}
