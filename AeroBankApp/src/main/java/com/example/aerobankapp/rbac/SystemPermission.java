package com.example.aerobankapp.rbac;

public enum SystemPermission
{
    DEPOSIT("Deposit"),
    WITHDRAW("Withdraw"),
    TRANSFER("Transfer"),
    PURCHASE("Purchase"),
    SETTINGS("Settings"),
    TRANSACTION_VIEW("Transaction View"),
    TRANSACTION_AUDIT_VIEW("Transaction Audit View"),
    SCHEDULER("Scheduler"),
    AUDIT_VIEW("Audit View"),

    SCHEDULER_ADMIN("Scheduler Admin"),
    SCHEDULER_USER("Scheduler User"),
    SCHEDULER_ALTERNATIVE("Scheduler Alternative");

    private String code;

    SystemPermission(String code)
    {
        this.code = code;
    }
}
