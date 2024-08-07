package com.example.aerobankapp.account;

public enum AccountType
{
    CHECKING("01"),
    SAVINGS("02"),
    INVESTMENT("04"),
    RENT("03"),
    MORTGAGE("05");
    private String code;

    AccountType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public static AccountType getInstance(String code)
    {
        AccountType type = null;
        switch(code)
        {
            case "01":
                type = CHECKING;
                break;
            case "02":
                type = SAVINGS;
                break;
            case "04":
                type = INVESTMENT;
                break;
            case "03":
                type = RENT;
                break;
            case "05":
                type = MORTGAGE;
                break;
            default:
                System.out.println("No Account of that type available");
        }
        return type;
    }
}
