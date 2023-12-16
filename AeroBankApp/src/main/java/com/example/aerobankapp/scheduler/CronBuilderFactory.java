package com.example.aerobankapp.scheduler;

public interface CronBuilderFactory
{
    String createCron(int interval, int min, int hour, int day, int month, int year);
}
