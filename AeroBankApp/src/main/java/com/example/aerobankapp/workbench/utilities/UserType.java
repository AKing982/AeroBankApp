package com.example.aerobankapp.workbench.utilities;

public enum UserType
{
    ADMIN("Admin"),
    MANAGER("Manager"),
    AUDITOR("Auditor"),
    TELLER("Teller"),
    USER("User");


    private String code;

    UserType(String code)
    {
        this.code = code;
    }
}
