package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.billPayment.NotificationMessageBuilder;

public class ProcessedBillPaymentNotificationSender extends SystemNotificationSender<AccountNotification>
{

    public ProcessedBillPaymentNotificationSender(AccountNotificationService accountNotificationService, NotificationMessageBuilder notificationMessageBuilder, EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder) {
        super(accountNotificationService, notificationMessageBuilder, accountNotificationEntityBuilder);
    }

    @Override
    boolean send(AccountNotification notification) {
        return false;
    }
}
