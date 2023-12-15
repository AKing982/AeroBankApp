package com.example.aerobankapp.scheduler.factory;

import org.quartz.Scheduler;

public interface AbstractSchedulerTypeFactory
{
    Scheduler createScheduler();
}
