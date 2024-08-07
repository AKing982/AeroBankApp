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
    private Long notificationID;
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

    public AccountNotificationResponse(Long notificationID, int accountID, String title, String message, int priority, boolean isRead, boolean isSevere, AccountNotificationCategory accountNotificationCategory) {
        this.notificationID = notificationID;
        this.accountID = accountID;
        this.title = title;
        this.message = message;
        this.priority = priority;
        this.isRead = isRead;
        this.isSevere = isSevere;
        this.accountNotificationCategory = accountNotificationCategory;
    }
}
