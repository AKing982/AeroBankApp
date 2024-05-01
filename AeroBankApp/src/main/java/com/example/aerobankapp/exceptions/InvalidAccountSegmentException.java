package com.example.aerobankapp.exceptions;

public class InvalidAccountSegmentException extends RuntimeException
{
    public InvalidAccountSegmentException(String message) {
        super(message);
    }
}
