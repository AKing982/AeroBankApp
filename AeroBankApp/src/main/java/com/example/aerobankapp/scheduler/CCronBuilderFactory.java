package com.example.aerobankapp.scheduler;

public interface CCronBuilderFactory
{
    String createCron(ScheduleType interval, int min, int hour, int day, int month, int year);
}
