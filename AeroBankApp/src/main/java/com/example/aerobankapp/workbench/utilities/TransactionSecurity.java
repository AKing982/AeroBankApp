package com.example.aerobankapp.workbench.utilities;

public enum TransactionSecurity
{
    WITHDRAW_ENABLED(true),
    DEPOSIT_ENABLED(true),
    PURCHASE_ENABLED(true),
    TRANSFER_ENABLED(true),
    WITHDRAW_DISABLED(false),
    DEPOSIT_DISABLED(false),
    PURCHASE_DISABLED(false),
    TRANSFER_DISABLED(false),

    MAX_WITHDRAWAL_LIMIT(1),
    MAX_DEPOSIT_LIMIT(1),

    DAILY_WITHDRAWAL_LIMIT(1),

    TRANSACTION_NOTIFICATION_ENABLED(true);

    private Object status;

    TransactionSecurity(Object code)
    {
        this.status = code;
    }

}
