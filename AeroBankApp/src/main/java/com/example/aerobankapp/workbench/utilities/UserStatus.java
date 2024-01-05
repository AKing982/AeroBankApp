package com.example.aerobankapp.workbench.utilities;


public enum UserStatus
{
    IS_ENABLED("Is Enabled"),
    IS_ADMIN("Is Admin");
    private String status;

    UserStatus(String code)
    {
        this.status = code;
    }
}
