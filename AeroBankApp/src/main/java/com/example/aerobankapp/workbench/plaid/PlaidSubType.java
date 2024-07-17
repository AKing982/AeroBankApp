package com.example.aerobankapp.workbench.plaid;

public enum PlaidSubType
{
    CHECKING("checking"),
    SAVINGS("savings"),
    PAYPAL("paypal"),
    CREDIT_CARD("credit card"),
    PAYABLE("payable"),
    STUDENT_LOAN("student"),
    MORTGAGE("mortgage"),
    AUTO("auto"),
    PERSONAL("personal"),
    BROKERAGE("brokerage"),
    IRA("ira"),
    MONEY_MARKET("money market");

    private String code;

    PlaidSubType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
}
