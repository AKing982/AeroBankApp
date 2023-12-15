package com.example.aerobankapp.scheduler.factory;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CustomSchedulerFactory implements AbstractSchedulerTypeFactory
{
    private final Scheduler scheduler;

    @Autowired
    public CustomSchedulerFactory(@Qualifier("scheduler") Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler createScheduler()
    {
        return null;
    }
}
