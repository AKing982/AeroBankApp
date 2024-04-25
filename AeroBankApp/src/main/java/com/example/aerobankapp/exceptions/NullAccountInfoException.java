package com.example.aerobankapp.exceptions;

import jakarta.validation.constraints.Null;

public class NullAccountInfoException extends NullPointerException
{
    public NullAccountInfoException(String s) {
        super(s);
    }
}
