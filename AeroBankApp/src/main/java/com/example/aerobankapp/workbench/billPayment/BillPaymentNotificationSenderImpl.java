package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.exceptions.InvalidProcessedBillPaymentException;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.AccountNotificationEntityBuilderImpl;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.utilities.AccountNotificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class BillPaymentNotificationSenderImpl implements BillPaymentNotificationSender
{
    private final AccountNotificationService accountNotificationService;
    private final NotificationMessageBuilder notificationMessageBuilder;
    private EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder;
    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentNotificationSenderImpl.class);

    @Autowired
    public BillPaymentNotificationSenderImpl(AccountNotificationService accountNotificationService,
                                             NotificationMessageBuilder notificationMessageBuilder,
                                             EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder){
        this.accountNotificationService = accountNotificationService;
        this.notificationMessageBuilder = notificationMessageBuilder;
        this.accountNotificationEntityBuilder = new AccountNotificationEntityBuilderImpl();
    }


    @Override
    public boolean sendPaymentNotification(BillPayment billPayment) {
        validateBillPayment(billPayment);


        return false;
    }

    @Override
    public boolean sendProcessedPaymentNotification(ProcessedBillPayment processedBillPayment) {
        validateProcessedPayment(processedBillPayment);

        StringBuilder paymentMessage = notificationMessageBuilder.buildProcessedPaymentNotification(processedBillPayment);
        if(paymentMessage == null){
            LOGGER.error("Payment message is null");
            return false;
        }
        LOGGER.info("Payment Message: {}", paymentMessage.toString());
        AccountNotification accountNotification = AccountNotificationUtil.buildAccountNotificationModel(paymentMessage, processedBillPayment);
        AccountNotificationEntity accountNotificationEntity = accountNotificationEntityBuilder.createEntity(accountNotification);
        if(accountNotificationEntity == null){
            LOGGER.error("Account Notification Entity is null. Unable to send payment notification.");
            return false;
        }
        try{

            accountNotificationService.save(accountNotificationEntity);
            return true;

        }catch(Exception e){
            LOGGER.error("There was an error saving the Account Notification to the server: {}", accountNotificationEntity, e);
            return false;
        }
    }



    private void validateProcessedPayment(ProcessedBillPayment processedBillPayment) {
        if (processedBillPayment == null) {
            throw new InvalidProcessedBillPaymentException("Processed payment is null");
        }
    }

    private void validateBillPayment(BillPayment billPayment) {
        if(billPayment == null){
            throw new IllegalArgumentException("BillPayment cannot be null");
        }
    }
}
