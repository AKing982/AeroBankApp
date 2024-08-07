package com.example.aerobankapp.scheduler.factory;

import lombok.Getter;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DailySchedulerFactory implements AbstractSchedulerTypeFactory
{
    private final Scheduler scheduler;
    private Trigger trigger;

    @Autowired
    public DailySchedulerFactory(@Qualifier("scheduler") Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }

    @Override
    public Scheduler createScheduler()
    {


        Scheduler scheduler = this.scheduler;
        return null;
    }
}
