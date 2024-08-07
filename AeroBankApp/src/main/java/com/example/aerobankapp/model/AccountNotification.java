package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.AccountNotificationCategory;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@Builder
public class AccountNotification extends Notification {

    private Long accountNotificationID;
    private int accountID;
    private AccountNotificationCategory category;

    public AccountNotification(String title, String message, int priority, boolean isRead, boolean isSevere) {
        super(title, message, priority, isRead, isSevere);
    }

    public AccountNotification(String title, String message, int priority, boolean isRead, boolean isSevere, Long accountNotificationID, int accountID, AccountNotificationCategory category) {
        super(title, message, priority, isRead, isSevere);
        this.accountNotificationID = accountNotificationID;
        this.accountID = accountID;
        this.category = category;
    }
}


