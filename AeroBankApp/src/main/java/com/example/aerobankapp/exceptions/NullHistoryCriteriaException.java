package com.example.aerobankapp.exceptions;

import jakarta.validation.constraints.Null;

public class NullHistoryCriteriaException extends RuntimeException
{
    public NullHistoryCriteriaException(String s) {
        super(s);
    }
}
