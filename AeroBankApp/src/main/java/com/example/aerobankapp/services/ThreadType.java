package com.example.aerobankapp.services;

public enum ThreadType
{
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    LOGIN("Login"),

    REGISTER("Register"),

    NONE("None"),

    NO_SUCH_INSTANCE("No Such Instance Exists");

    private String code;

    ThreadType(String code)
    {
        this.code = code;
    }

    public ThreadType getInstance(String code)
    {
        switch(code)
        {
            case "Login":
                return LOGIN;
            case "Register":
                return REGISTER;
            case "Withdraw":
                return WITHDRAW;
            case "Deposit":
                return DEPOSIT;
            default:
                return NO_SUCH_INSTANCE;
        }
    }


}
