package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.workbench.billPayment.NotificationUtil;


public class AccountNotificationUtil
{
    private AccountNotificationUtil(){

    }

    public static AccountNotification buildAccountNotificationModel(StringBuilder message, ProcessedBillPayment processedBillPayment) {
        String payeeName = processedBillPayment.getBillPayment().getPayeeName();
        int acctID = processedBillPayment.getBillPayment().getAccountCode().getSequence();
        return NotificationUtil.buildAccountNotificationModel(message, payeeName, acctID);
    }
}
