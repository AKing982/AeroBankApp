package com.example.aerobankapp.workbench.plaid;

public enum PlaidSubType
{
    CHECKING("checking"),
    SAVINGS("savings"),
    PAYPAL("paypal"),
    MONEY_MARKET("money_market");

    private String code;

    PlaidSubType(String code)
    {
        this.code = code;
    }
}
