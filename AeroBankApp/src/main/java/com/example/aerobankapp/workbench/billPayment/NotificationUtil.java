package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.workbench.AccountNotificationCategory;

public class NotificationUtil
{
    private NotificationUtil(){

    }

    public static AccountNotification buildAccountNotificationModel(StringBuilder strMessage, String payeeName, int acctID){
        return AccountNotification.builder()
                .accountID(acctID)
                .title(payeeName)
                .message(strMessage.toString())
                .category(AccountNotificationCategory.PAYMENT_PROCESSED)
                .isRead(false)
                .isSevere(false)
                .priority(1)
                .build();
    }
}
