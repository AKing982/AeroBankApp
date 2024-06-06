package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.billPayment.NotificationMessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class LatePaymentNotificationSender extends SystemNotificationSender<AccountNotification>
{

    public LatePaymentNotificationSender(AccountNotificationService accountNotificationService, NotificationMessageBuilder notificationMessageBuilder, EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder) {
        super(accountNotificationService, notificationMessageBuilder, accountNotificationEntityBuilder);
    }

    @Override
    boolean send(AccountNotification notification) {
        return false;
    }

}
