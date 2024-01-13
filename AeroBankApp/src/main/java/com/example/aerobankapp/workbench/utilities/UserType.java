package com.example.aerobankapp.workbench.utilities;

public enum UserType
{
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    AUDITOR("AUDITOR"),
    TELLER("TELLER"),
    USER("USER");


    private String code;

    UserType(String code)
    {
        this.code = code;
    }
}
