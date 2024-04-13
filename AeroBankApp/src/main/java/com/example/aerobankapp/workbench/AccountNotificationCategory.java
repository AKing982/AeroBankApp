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
    ACCOUNT_UPDATE("AccountUpdate");

    private String code;

    AccountNotificationCategory(String code){
        this.code = code;
    }
}
