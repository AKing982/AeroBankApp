package com.example.aerobankapp.workbench.billPayment;

public interface BillPaymentNotificationSender<T>
{
    boolean send(T payment);
}
