package com.example.aerobankapp.workbench.utilities;

import lombok.Getter;

@Getter
public enum TransactionStatus
{
    PENDING("Pending"),

    COMPLETED("Completed"),
    FAILED("Failed"),

    CANCELED("Canceled");

    private String code;

    TransactionStatus(String code){
        this.code = code;
    }
}
