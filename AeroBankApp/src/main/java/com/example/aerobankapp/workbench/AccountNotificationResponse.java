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

    private String title;
    private String message;
    private int priority;
    private boolean isRead;
    private boolean isSevere;
    private AccountNotificationCategory accountNotificationCategory;

    public AccountNotificationResponse(int acctID, String title, String msg, int priority, boolean isRead, boolean isSevere, AccountNotificationCategory category){
        this.accountID = acctID;
        this.title = title;
        this.message = msg;
        this.priority = priority;
        this.isRead = isRead;
        this.isSevere = isSevere;
        this.accountNotificationCategory = category;
    }
}
