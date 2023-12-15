package com.example.aerobankapp.scheduler;

import org.springframework.stereotype.Component;

@Component
public enum ScheduleType
{
    BIWEEKLY("Bi Weekly"),
    MONTHLY("Monthly"),
    WEEKLY("Weekly"),
    DAILY("Daily"),
    CUSTOM("Custom"),

    NONE("None"),

    EVERY_TWO_DAYS("Every Two Days");

    private String schedule;

    ScheduleType(String type)
    {
        this.schedule = type;
    }
}
