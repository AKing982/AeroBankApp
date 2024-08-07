package com.example.aerobankapp.workbench.plaid;

public enum PlaidAccountType
{
    DEPOSITORY("depository"),
    CREDIT("credit"),
    LOAN("loan"),
    INVESTMENT("investment"),
    OTHER("other");

    private String code;

    PlaidAccountType(String code)
    {
        this.code = code;
    }
}
