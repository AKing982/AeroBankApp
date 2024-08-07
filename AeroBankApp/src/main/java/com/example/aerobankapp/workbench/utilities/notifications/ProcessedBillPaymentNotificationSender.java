package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.exceptions.InvalidProcessedBillPaymentException;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.billPayment.NotificationMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessedBillPaymentNotificationSender extends SystemNotificationSender<AccountNotification, ProcessedBillPayment>
{
    private NotificationStrategy<ProcessedBillPayment> notificationStrategy;
    private Logger LOGGER = LoggerFactory.getLogger(ProcessedBillPaymentNotificationSender.class);

    public ProcessedBillPaymentNotificationSender(AccountNotificationService accountNotificationService, EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder) {
        super(accountNotificationService, accountNotificationEntityBuilder);
        this.notificationStrategy = new ProcessedBillPaymentMessageStrategy();
    }

    @Override
    public StringBuilder createMessage(ProcessedBillPayment payment) {
        validatePayment(payment);
        StringBuilder paymentMessage = notificationStrategy.buildMessage(payment);
        if(paymentMessage == null){
            LOGGER.error("Payment message is null");
        }else{
            LOGGER.error("Payment Message: {}", paymentMessage.toString());
        }
        return paymentMessage;
    }

    @Override
    public boolean send(AccountNotification notification) {
        AccountNotificationEntity accountNotificationEntity = accountNotificationEntityBuilder.createEntity(notification);
        if(accountNotificationEntity == null){
            LOGGER.error("Account notification entity is null. Unable to send payment notification.");
        }

        try{
            accountNotificationService.save(accountNotificationEntity);
            return true;

        }catch(Exception e){
            LOGGER.error("There was an error saving the Account Notification to the server: {}", accountNotificationEntity, e);
            return false;
        }
    }

    @Override
    void validatePayment(ProcessedBillPayment payment) {
        if(payment == null){
            throw new InvalidProcessedBillPaymentException("Found null processed payment");
        }
    }
}
