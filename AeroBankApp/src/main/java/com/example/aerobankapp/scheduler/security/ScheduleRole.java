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

    private String scheduleRole;

    ScheduleRole(String role)
    {
        this.scheduleRole = role;
    }

    public ScheduleRole getScheduleRole(String role)
    {
        return switch (role) {
            case "Schedule Admin" -> SADMIN;
            case "Schedule User" -> SUSER;
            case "Schedule Teller" -> STELLER;
            case "Schedule Manager" -> SMANAGER;
            default -> NONE;
        };
    }

}
