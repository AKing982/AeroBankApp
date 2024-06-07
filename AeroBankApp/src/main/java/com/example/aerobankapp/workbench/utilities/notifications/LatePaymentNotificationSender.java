package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.exceptions.InvalidAccountNotificationException;
import com.example.aerobankapp.exceptions.InvalidLatePaymentException;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.EntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LatePaymentNotificationSender extends SystemNotificationSender<AccountNotification, LateBillPayment>
{
    private NotificationStrategy<LateBillPayment> notificationStrategy;
    private Logger LOGGER = LoggerFactory.getLogger(LatePaymentNotificationSender.class);

    public LatePaymentNotificationSender(AccountNotificationService accountNotificationService, EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder) {
        super(accountNotificationService, accountNotificationEntityBuilder);
        this.notificationStrategy = new LatePaymentMessageStrategy();
    }

    @Override
    public StringBuilder createMessage(LateBillPayment payment) {
        if(payment == null){
            throw new InvalidLatePaymentException("Late Payment cannot be null.");
        }
        return notificationStrategy.buildMessage(payment);
    }


    @Override
    public boolean send(AccountNotification notification) {
        if(notification == null){
            throw new InvalidAccountNotificationException("Account Notification cannot be null.");
        }
        AccountNotificationEntity accountNotificationEntity = accountNotificationEntityBuilder.createEntity(notification);
        try{
            accountNotificationService.save(accountNotificationEntity);
            return true;
        }catch(Exception e){
            LOGGER.error("There was an error while sending the notification.", e);
            return false;
        }
    }

    @Override
    void validatePayment(LateBillPayment payment) {

    }

}
