package com.example.aerobankapp.workbench.utilities;

public enum TransactionSecurity
{
    WITHDRAW_ENABLED("Withdraw-Enabled"),
    DEPOSIT_ENABLED("Deposit-Enabled"),
    PURCHASE_ENABLED("Purchase-Enabled"),
    TRANSFER_ENABLED("Transfer-Enabled"),
    WITHDRAW_DISABLED("Withdraw-Disabled"),
    DEPOSIT_DISABLED("Deposit-Disabled"),
    PURCHASE_DISABLED("Purchase-Disabled"),
    TRANSFER_DISABLED("Transfer-Disabled");
    private String status;

    TransactionSecurity(String code)
    {
        this.status = code;
    }

}
