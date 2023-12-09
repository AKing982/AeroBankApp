package com.example.aerobankapp.workbench.utilities;

public enum UserType
{
    ADMIN("Admin"),
    USER("User"),
    AUDITOR("Auditor"),
    MANAGER("Manager"),
    TELLER("Teller");


    private String code;

    UserType(String code)
    {
        this.code = code;
    }
}
