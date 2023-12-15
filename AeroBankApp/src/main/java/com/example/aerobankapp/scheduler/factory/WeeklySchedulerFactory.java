package com.example.aerobankapp.scheduler.factory;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeeklySchedulerFactory implements AbstractSchedulerTypeFactory
{
    @Autowired
    private Scheduler scheduler;

    @Override
    public Scheduler createScheduler()
    {
        return null;
    }
}
