package com.example.aerobankapp.scheduler.factory;

import com.example.aerobankapp.scheduler.factory.trigger.WeeklyTriggerFactory;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Getter
public class WeeklySchedulerFactory implements AbstractSchedulerTypeFactory
{
    private final Scheduler scheduler;
    private final WeeklyTriggerFactory weeklyTriggerFactory;

    @Autowired
    public WeeklySchedulerFactory(@Qualifier("scheduler") Scheduler scheduler, @Qualifier("weeklyTriggerFactory")WeeklyTriggerFactory weeklyTriggerFactory)
    {
        this.scheduler = scheduler;
        this.weeklyTriggerFactory = weeklyTriggerFactory;
    }

    @Override
    public Scheduler createScheduler()
    {
        CronTrigger weeklyTrigger = getWeeklyTriggerFactory().createCronTrigger();

    }
}
