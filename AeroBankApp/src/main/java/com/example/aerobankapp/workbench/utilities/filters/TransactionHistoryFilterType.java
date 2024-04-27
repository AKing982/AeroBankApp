package com.example.aerobankapp.workbench.utilities.filters;

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

}
