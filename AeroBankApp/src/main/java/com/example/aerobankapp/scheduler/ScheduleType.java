package com.example.aerobankapp.scheduler;

import org.springframework.stereotype.Component;


public enum ScheduleType
{
    BIWEEKLY("Bi-Weekly"),
    MONTHLY("Monthly"),
    WEEKLY("Weekly"),
    DAILY("Daily"),
    CUSTOM("Custom"),

    NONE("None"),

    BIDAILY("Bi-Daily");

    private String schedule;

    ScheduleType(String type)
    {
        this.schedule = type;
    }
}
