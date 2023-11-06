package com.example.aerobankapp.workbench.utilities;

public enum InvestmentType
{
    GROWTH("Growth"),
    INCOME("Income"),

    CAPITAL_PRES("Capital Preservation");

    private String code;

    InvestmentType(String code)
    {
        this.code = code;
    }

    public InvestmentType getInvestmentType(String code)
    {
        switch(code)
        {
            case "Growth":
                return GROWTH;
            case"Capital":
                return CAPITAL_PRES;
            case "Income":
                return INCOME;
            default:
                return null;
        }
    }
}
