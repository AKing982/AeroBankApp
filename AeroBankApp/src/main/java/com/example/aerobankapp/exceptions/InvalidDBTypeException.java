package com.example.aerobankapp.exceptions;

public class InvalidDBTypeException extends IllegalArgumentException
{
    public InvalidDBTypeException(String s) {
        super(s);
    }
}
