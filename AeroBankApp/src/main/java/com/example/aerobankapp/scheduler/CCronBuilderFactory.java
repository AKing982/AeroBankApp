package com.example.aerobankapp.scheduler;

public interface CCronBuilderFactory
{
    String createCron(int interval, int min, int hour, int day, int month, int year);
}
