package com.example.aerobankapp.exceptions;

public class NonEmptyListRequiredException extends IllegalArgumentException
{
    public NonEmptyListRequiredException(String s) {
        super(s);
    }
}
