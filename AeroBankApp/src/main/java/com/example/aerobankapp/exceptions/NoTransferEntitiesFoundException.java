package com.example.aerobankapp.exceptions;

public class NoTransferEntitiesFoundException extends RuntimeException
{
    public NoTransferEntitiesFoundException(String message) {
        super(message);
    }
}
