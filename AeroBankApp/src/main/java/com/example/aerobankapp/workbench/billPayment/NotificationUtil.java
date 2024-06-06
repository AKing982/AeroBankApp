package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.workbench.AccountNotificationCategory;

public class NotificationUtil
{
    private NotificationUtil(){

    }

    public static AccountNotification buildAccountNotificationModel(StringBuilder strMessage, String payeeName, int acctID){
        AccountNotification accountNotification = new AccountNotification();
        accountNotification.setAccountID(acctID);
        accountNotification.setCategory(AccountNotificationCategory.PAYMENT_PROCESSED);
        accountNotification.setMessage(payeeName);
        accountNotification.setRead(false);
        accountNotification.setPriority(1);
        accountNotification.setTitle(payeeName);
        accountNotification.setMessage(strMessage.toString());
        accountNotification.setSevere(false);
        return accountNotification;
    }
}
