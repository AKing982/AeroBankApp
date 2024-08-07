package com.example.aerobankapp.exceptions;

public class InvalidPlaidSubTypeException extends RuntimeException
{
    public InvalidPlaidSubTypeException(String subType) {
        super(subType + " is not a valid plaid subtype");
    }
}
