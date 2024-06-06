package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.model.AccountNotification;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.billPayment.NotificationMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BillPaymentNotificationSender extends SystemNotificationSender<AccountNotification, BillPayment>
{
    private NotificationStrategy<BillPayment> billPaymentNotificationStrategy;
    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentNotificationSender.class);

    public BillPaymentNotificationSender(AccountNotificationService accountNotificationService, EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder) {
        super(accountNotificationService, accountNotificationEntityBuilder);
        this.billPaymentNotificationStrategy = new BillPaymentNotificationStrategy();
    }

    @Override
    public StringBuilder createMessage(final BillPayment payment) {
        validatePayment(payment);
        StringBuilder paymentMessage = billPaymentNotificationStrategy.buildMessage(payment);
        if(paymentMessage == null){
            LOGGER.error("Payment message is null");
        }
        return paymentMessage;
    }

    @Override
    public boolean send(AccountNotification notification) {
        return false;
    }

    @Override
    void validatePayment(BillPayment payment) {
        if(payment == null){
            throw new InvalidBillPaymentException("Invalid bill payment");
        }
    }


}
