package com.example.aerobankapp.scheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PurchaseScheduler extends SchedulerEngineBase
{

    public PurchaseScheduler(@Qualifier("scheduler") Scheduler scheduler)
    {
        super(scheduler);
    }

    @Override
    protected Scheduler getDailySimpleScheduler() {
        return null;
    }

    @Override
    protected Scheduler getWeeklySimpleScheduler() {
        return null;
    }

    @Override
    protected Scheduler getBiWeeklySimpleScheduler() {
        return null;
    }

    @Override
    protected Scheduler getWeeklyCronScheduler() {
        return null;
    }

    @Override
    protected Scheduler getMonthlyCronScheduler() {
        return null;
    }

    @Override
    protected Scheduler getCustomCronScheduler() throws SchedulerException {
        return null;
    }


}
