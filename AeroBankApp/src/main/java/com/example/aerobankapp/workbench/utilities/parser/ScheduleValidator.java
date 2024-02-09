package com.example.aerobankapp.workbench.utilities.parser;

import com.example.aerobankapp.scheduler.ScheduleType;

public interface ScheduleValidator
{
    int validateDay(int day);
    int validateMonth(int month);
    int validateYear(int year);
    int validateMinute(int minute);
    int validateHour(int hour);
    ScheduleType validateInterval(ScheduleType interval);
}
