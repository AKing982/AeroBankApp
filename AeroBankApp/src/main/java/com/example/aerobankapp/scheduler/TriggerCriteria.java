package com.example.aerobankapp.scheduler;

import lombok.*;
import org.springframework.stereotype.Component;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TriggerCriteria
{
    private int day;
    private int minute;
    private int hour;
    private int second;
    private int month;
    private int year;
    private int interval;
    private int repeat;
    private boolean isCron;
    private boolean isWeekly;
    private boolean isMonthly;
    private boolean isDaily;

    public TriggerCriteria(int interval, int minute, int hour)
    {
        this.interval = interval;
        this.minute = minute;
        this.hour = hour;
    }

    public TriggerCriteria(int interval, int hour, int min, int day, int month, int year)
    {
        this.interval = interval;
        this.hour = hour;
        this.minute = min;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public TriggerCriteria(int interval, int min, int hour, int day)
    {
        this.interval = interval;
        this.minute = min;
        this.hour = hour;
        this.day = day;
    }
}
