package com.example.aerobankapp.exceptions;

public class UserAlreadyExistsException extends RuntimeException
{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
