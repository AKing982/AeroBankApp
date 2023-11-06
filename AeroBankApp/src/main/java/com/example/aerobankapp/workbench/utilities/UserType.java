package com.example.aerobankapp.workbench.utilities;

public enum UserType
{
    ADMIN("Admin"),
    USER("User");


    private String code;

    UserType(String code)
    {
        this.code = code;
    }
}
