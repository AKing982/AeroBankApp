package com.example.aerobankapp.scheduler;

import org.springframework.stereotype.Component;


public enum ScheduleType
{
    BIWEEKLY("Bi-Weekly"),
    MONTHLY("Monthly"),
    WEEKLY("Weekly"),
    DAILY("Daily"),
    CUSTOM("Custom"),

    Once("Once"),

    Daily("Daily"),
    Weekly("Weekly"),
    Monthly("Monthly"),

    ONCE("Once"),

    NONE("None"),

    BIDAILY("Bi-Daily");

    private String schedule;

    ScheduleType(String type)
    {
        this.schedule = type;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
