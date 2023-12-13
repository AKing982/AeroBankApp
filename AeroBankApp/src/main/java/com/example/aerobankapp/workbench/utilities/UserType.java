package com.example.aerobankapp.workbench.utilities;

public enum UserType
{
    ADMIN("Admin"),
    USER("User");

    private String userRole;

    UserType(String role)
    {
        this.userRole = role;
    }
}
