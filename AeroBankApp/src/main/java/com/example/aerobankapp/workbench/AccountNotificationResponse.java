package com.example.aerobankapp.workbench;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class AccountNotificationResponse implements Serializable
{
    private int accountID;
    private String message;
    private int priority;

    public AccountNotificationResponse(int acctID, String msg, int priority){
        this.accountID = acctID;
        this.message = msg;
        this.priority = priority;
    }
}
