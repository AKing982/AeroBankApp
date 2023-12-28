package com.example.aerobankapp.scheduler;

import lombok.*;
import org.springframework.stereotype.Component;

@Builder
@Getter
@Component
@AllArgsConstructor
@NoArgsConstructor
public final class TriggerCriteria {

    private int day;
    private int minute;
    private int hour;
    private int second;
    private int month;
    private int year;
    private int interval;
    private int repeat;
    private boolean isCron;

    public TriggerCriteria(int interval, int min, int hour, int day)
    {
        this.interval = interval;
        this.minute = min;
        this.hour = hour;
        this.day = day;
    }

    public TriggerCriteria(int interval, int min, int hour)
    {
        this.interval = interval;
        this.minute = min;
        this.hour = hour;
    }

    public TriggerCriteria(int interval, int min, int hour, int day, int month, int year)
    {
        this.interval = interval;
        this.minute = min;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
