package com.example.aerobankapp.model;

import lombok.Data;

@Data
public class PlaidImportResult
{
    private Object result;
    private boolean isSuccessful;

    public PlaidImportResult(Object result, boolean isSuccessful)
    {
        this.result = result;
        this.isSuccessful = isSuccessful;
    }
}
