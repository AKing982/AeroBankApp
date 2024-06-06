package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.billPayment.NotificationMessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class BillPaymentNotificationSender extends SystemNotificationSender<AccountNotification>
{

    public BillPaymentNotificationSender(AccountNotificationService accountNotificationService, NotificationMessageBuilder notificationMessageBuilder, EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder) {
        super(accountNotificationService, notificationMessageBuilder, accountNotificationEntityBuilder);
    }

    @Override
    public boolean send(AccountNotification notification) {
        return false;
    }
}
