package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.Notification;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.AccountNotificationEntityBuilderImpl;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.billPayment.NotificationMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class SystemNotificationSender<M extends Notification, T>
{
    protected AccountNotificationService accountNotificationService;
    protected EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder;


    @Autowired
    public SystemNotificationSender(AccountNotificationService accountNotificationService,
                                    EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder){
        this.accountNotificationService = accountNotificationService;
        this.accountNotificationEntityBuilder = new AccountNotificationEntityBuilderImpl();
    }

    abstract StringBuilder createMessage(T payment);

    abstract boolean send(M notification);

    abstract void validatePayment(T payment);
}
