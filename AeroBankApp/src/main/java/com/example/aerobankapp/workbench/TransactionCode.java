package com.example.aerobankapp.workbench;

import lombok.Getter;

@Getter
public enum TransactionCode
{
    DEPOSIT("Deposit Transfer"),

    WITHDRAW("Withdraw Transfer"),

    TRANSFER("User Transfer"),

    PURCHASE("PIN Purchase");

    private String code;

    TransactionCode(String code){
        this.code = code;
    }

}
