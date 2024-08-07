package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.MissedBillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.billPayment.NotificationMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class MissedPaymentNotificationSender extends SystemNotificationSender<AccountNotification, MissedBillPayment>
{


    public MissedPaymentNotificationSender(AccountNotificationService accountNotificationService, EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder) {
        super(accountNotificationService, accountNotificationEntityBuilder);
    }

    @Override
    StringBuilder createMessage(MissedBillPayment payment) {
        return null;
    }


    @Override
    boolean send(AccountNotification notification) {
        return false;
    }

    @Override
    void validatePayment(MissedBillPayment payment) {

    }


}
