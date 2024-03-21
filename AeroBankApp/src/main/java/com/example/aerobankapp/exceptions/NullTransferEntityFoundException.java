package com.example.aerobankapp.exceptions;

import jakarta.validation.constraints.Null;

public class NullTransferEntityFoundException extends NullPointerException
{
    public NullTransferEntityFoundException(String s) {
        super(s);
    }
}
