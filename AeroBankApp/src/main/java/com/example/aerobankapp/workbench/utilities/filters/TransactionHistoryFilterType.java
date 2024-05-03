package com.example.aerobankapp.workbench.utilities.filters;

import com.example.aerobankapp.exceptions.InvalidTransactionHistoryFilterException;
import lombok.Getter;

@Getter
public enum TransactionHistoryFilterType
{
    DESCRIPTION("Description"),
    DATE("Date"),
    TRANSACTION_TYPE("TransactionType"),
    STATUS("Status"),
    USERNAME("UserName"),
    ACCOUNTID("AccountID"),
    AMOUNT_RANGE("AmountRange");

    private String code;

    TransactionHistoryFilterType(String type){
        this.code = type;
    }

    public static TransactionHistoryFilterType getInstance(String code){
        switch(code){
            case "Description":
                return DESCRIPTION;
            case "Date":
                return DATE;
            case "TransactionType":
                return TRANSACTION_TYPE;
            case "Status":
                return STATUS;
            case "AmountRange":
                return AMOUNT_RANGE;
            default:
                throw new InvalidTransactionHistoryFilterException("Invalid filter type.");
        }
    }

}
