package com.example.aerobankapp.exceptions;

public class InvalidDatabaseNameException extends IllegalArgumentException
{
    public InvalidDatabaseNameException(String s) {
        super(s);
    }
}
