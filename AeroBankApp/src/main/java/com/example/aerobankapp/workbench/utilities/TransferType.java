package com.example.aerobankapp.workbench.utilities;

public enum TransferType
{
    USER_TO_USER("UserToUser"),
    SAME_USER("SameUser");
    private String code;

    TransferType(String code){
        this.code = code;
    }
}
