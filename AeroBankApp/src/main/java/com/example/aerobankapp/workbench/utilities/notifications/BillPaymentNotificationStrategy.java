package com.example.aerobankapp.workbench.utilities.notifications;

import com.example.aerobankapp.model.BillPayment;

public class BillPaymentNotificationStrategy implements NotificationStrategy<BillPayment>
{

    @Override
    public StringBuilder buildMessage(BillPayment payment) {
        return null;
    }
}
