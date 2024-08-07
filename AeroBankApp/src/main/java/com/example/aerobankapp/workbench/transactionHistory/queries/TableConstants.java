package com.example.aerobankapp.workbench.transactionHistory.queries;

import com.example.aerobankapp.exceptions.InvalidTableTypeFoundException;
import org.springframework.security.core.parameters.P;

public enum TableConstants
{
    DEPOSITS("Deposit"),

    WITHDRAWAL("Withdraw"),

    TRANSFER("Transfer"),

    PURCHASE("Purchase");

    private String code;

    TableConstants(String code){
        this.code = code;
    }

    public static TableConstants getInstance(String code){
        return switch (code) {
            case "Deposit" -> DEPOSITS;
            case "Withdraw" -> WITHDRAWAL;
            case "Transfer" -> TRANSFER;
            case "Purchase" -> PURCHASE;
            default -> throw new InvalidTableTypeFoundException("Table Type not found.");
        };
    }
}
