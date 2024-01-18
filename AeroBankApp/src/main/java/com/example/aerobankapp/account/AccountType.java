package com.example.aerobankapp.account;

import org.springframework.stereotype.Component;

public enum AccountType
{
    CHECKING("Checking"),
    SAVINGS("Savings"),
    INVESTMENT("Investment"),
    RENT("Rent"),
    MORTGAGE("Mortgage");
    private String code;

    AccountType(String code)
    {
        this.code = code;
    }

    public static AccountType getInstance(String code)
    {
        AccountType type = null;
        switch(code)
        {
            case "Checking":
                type = CHECKING;
                break;
            case "Savings":
                type = SAVINGS;
                break;
            case "Investment":
                type = INVESTMENT;
                break;
            case "Rent":
                type = RENT;
                break;
            case "Mortage":
                type = MORTGAGE;
                break;
            default:
                System.out.println("No Account of that type available");
        }
        return type;
    }
}
