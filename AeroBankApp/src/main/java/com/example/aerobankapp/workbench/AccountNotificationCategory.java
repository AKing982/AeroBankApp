package com.example.aerobankapp.workbench;

public enum AccountNotificationCategory
{
    TRANSACTION_ALERT("TransactionAlert"),
    BALANCE_UPDATE("BalanceUpdate"),
    ACCOUNT_SECURITY("AccountSecurity"),
    ACCOUNT_ALERT("AccountAlert"),
    PAYMENT_REMINDER("PaymentReminder"),
    PAYMENT_RECEIVED("PaymentReceived"),
    SCHEDULED_MAINTENANCE("ScheduledMaintenance"),
    SYSTEM_TEST("SystemTest"),
    ACCOUNT_UPDATE("AccountUpdate"),
    PAYMENT_PROCESSED("PaymentProcessed");

    private String code;

    AccountNotificationCategory(String code){
        this.code = code;
    }
}
