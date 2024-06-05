package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;

public interface BillPaymentNotificationSender
{
    boolean sendPaymentNotification(BillPayment billPayment);

    boolean sendProcessedPaymentNotification(ProcessedBillPayment processedBillPayment);
}
