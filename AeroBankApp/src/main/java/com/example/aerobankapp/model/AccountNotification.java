package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.AccountNotificationCategory;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class AccountNotification {

    private Long accountNotificationID;
    private int accountID;
    private String title;
    private String message;
    private int priority;
    private boolean isRead;
    private boolean isSevere;
    private AccountNotificationCategory category;

}


