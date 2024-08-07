package com.example.aerobankapp.scheduler.security;

import org.springframework.security.core.parameters.P;

public enum ScheduleRole
{
    SADMIN("Schedule Admin"),
    SAUDITOR("Schedule Auditor"),

    NONE("No Role Fits this criteria"),
    SUSER("Schedule User"),

    STELLER("Schedule Teller"),

    SMANAGER("Schedule Manager");

    private String code;

    ScheduleRole(String code)
    {
        this.code = code;
    }

    public ScheduleRole getScheduleRole(String code)
    {
        return switch (code) {
            case "Schedule Admin" -> SADMIN;
            case "Schedule User" -> SUSER;
            case "Schedule Teller" -> STELLER;
            case "Schedule Manager" -> SMANAGER;
            default -> NONE;
        };
    }

}
