package com.example.aerobankapp.workbench.utilities;

import org.springframework.security.core.parameters.P;

public enum AccountStatus
{
    EXPIRED("Expired"),
    NON_EXPIRED("Non-Expired"),
    ENABLED("Enabled"),
    DISABLED("Disabled"),
    LOCKED("Locked"),
    NON_LOCKED("Non-Locked");
    private String status;

    AccountStatus(String code)
    {
        this.status = code;
    }

}
