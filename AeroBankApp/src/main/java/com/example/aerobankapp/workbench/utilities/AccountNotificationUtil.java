package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.workbench.billPayment.NotificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccountNotificationUtil
{
    private static Logger LOGGER = LoggerFactory.getLogger(AccountNotificationUtil.class);

    private AccountNotificationUtil(){

    }

    public static AccountNotification buildAccountNotificationModel(StringBuilder message, ProcessedBillPayment processedBillPayment) {
        String payeeName = processedBillPayment.getBillPayment().getPayeeName();
        int acctID = processedBillPayment.getBillPayment().getAccountCode().getSequence();
        LOGGER.info("Payment Message: {}", message.toString());
        return NotificationUtil.buildAccountNotificationModel(message, payeeName, acctID);
    }

    public static AccountNotification buildAccountNotificationModel(StringBuilder message, LateBillPayment lateBillPayment){
        String payeeName = lateBillPayment.getBillPayment().getPayeeName();
        int acctID = lateBillPayment.getBillPayment().getAccountCode().getSequence();
        return NotificationUtil.buildAccountNotificationModel(message, payeeName, acctID);
    }
}
