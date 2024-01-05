package com.example.aerobankapp.workbench.security.authentication;

public enum SecurityType
{
    BASIC("Basic"),
    DB("DB"),
    MFA("MFA"),
    OAUTH("OAuth"),
    HTTPS("Https");
    private String type;

    SecurityType(String code)
    {
        this.type = code;
    }
}
