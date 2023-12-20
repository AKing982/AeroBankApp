package com.example.aerobankapp.scheduler.factory;

import com.example.aerobankapp.scheduler.factory.trigger.CronTriggerFactory;
import com.example.aerobankapp.scheduler.factory.trigger.MonthlyTriggerFactory;
import com.example.aerobankapp.scheduler.factory.trigger.WeeklyTriggerFactory;
import lombok.Getter;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MonthlySchedulerFactory implements AbstractSchedulerTypeFactory
{
    private final Scheduler scheduler;
    private final MonthlyTriggerFactory monthlyTriggerFactory;

    @Autowired
    public MonthlySchedulerFactory(@Qualifier("scheduler") Scheduler scheduler, @Qualifier("monthlyTriggerFactory") MonthlyTriggerFactory monthlyTriggerFactory)
    {
        this.scheduler = scheduler;
        this.monthlyTriggerFactory = monthlyTriggerFactory;
    }


    @Override
    public Scheduler createScheduler()
    {
        CronTrigger monthlyTrigger = getMonthlyTriggerFactory().createCronTrigger();
        //JobDetail jobDetail =
        return scheduler;
    }
}
