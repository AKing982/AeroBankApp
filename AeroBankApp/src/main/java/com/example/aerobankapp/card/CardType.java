package com.example.aerobankapp.card;

public enum CardType
{
    VISA("Visa"),
    MASTERCARD("MasterCard"),
    DISCOVER("Discover"),
    AMEX("American Express");

    private String code;

    CardType(String code)
    {
        this.code = code;
    }

    public CardType getCode(String code)
    {
        switch(code)
        {
            case "Visa":
                return VISA;
            case "MasterCard":
                return MASTERCARD;
            case "Discover":
                return DISCOVER;
            case "American Express":
                return AMEX;
        }
        return null;
    }
}
