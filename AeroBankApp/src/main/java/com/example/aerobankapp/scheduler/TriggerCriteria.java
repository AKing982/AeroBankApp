package com.example.aerobankapp.scheduler;

import lombok.*;
import org.springframework.stereotype.Component;

@Builder
@NoArgsConstructor
public class TriggerCriteria {

    private int day;
    private int minute;
    private int hour;
    private int second;
    private int month;
    private int year;
    private ScheduleType interval;
    private int repeat;
    private boolean isCron;

    public TriggerCriteria(int day, int minute, int hour, int second, int month, int year, ScheduleType interval, int repeat, boolean isCron) {
        this.day = day;
        this.minute = minute;
        this.hour = hour;
        this.second = second;
        this.month = month;
        this.year = year;
        this.interval = interval;
        this.repeat = repeat;
        this.isCron = isCron;
    }

    public TriggerCriteria(ScheduleType interval, int min, int hour, int day)
    {
        this.interval = interval;
        this.minute = min;
        this.hour = hour;
        this.day = day;
    }

    public TriggerCriteria(ScheduleType interval, int min, int hour)
    {
        this.interval = interval;
        this.minute = min;
        this.hour = hour;
    }

    public TriggerCriteria(ScheduleType interval, int min, int hour, int day, int month, int year)
    {
        this.interval = interval;
        this.minute = min;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ScheduleType getInterval() {
        return interval;
    }

    public void setInterval(ScheduleType interval) {
        this.interval = interval;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public boolean isCron() {
        return isCron;
    }

    public void setCron(boolean cron) {
        isCron = cron;
    }
}
