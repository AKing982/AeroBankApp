package com.example.aerobankapp.scheduler.factory;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DailySchedulerFactory implements AbstractSchedulerTypeFactory
{
    private final Scheduler scheduler;
    private final Trigger dailyTrigger;

    @Autowired
    public DailySchedulerFactory(@Qualifier("scheduler") Scheduler scheduler, JobDetail jobDetail, @Qualifier("dailyDepositSimpleTriggerBean") Trigger trigger)
    {
        this.scheduler = scheduler;
        this.dailyTrigger = trigger;
    }

    @Override
    public Scheduler createScheduler()
    {
        Scheduler scheduler = this.scheduler;
        return null;
    }
}
