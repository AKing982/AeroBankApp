package com.example.aerobankapp.scheduler.factory;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BiWeeklySchedulerFactory implements AbstractSchedulerTypeFactory
{
    private Scheduler scheduler;

    @Autowired
    public BiWeeklySchedulerFactory(Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler createScheduler() {
        return null;
    }
}
