package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.billPayment.NotificationMessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class LatePaymentNotificationSender extends SystemNotificationSender<AccountNotification, LateBillPayment>
{
    private NotificationStrategy<LateBillPayment> notificationStrategy;

    public LatePaymentNotificationSender(AccountNotificationService accountNotificationService, EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder) {
        super(accountNotificationService, accountNotificationEntityBuilder);
    }

    @Override
    public StringBuilder createMessage(LateBillPayment payment) {
        return null;
    }


    @Override
    boolean send(AccountNotification notification) {
        return false;
    }

    @Override
    void validatePayment(LateBillPayment payment) {

    }

}
