package com.example.aerobankapp.scheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public class TransferScheduler extends SchedulerEngineBase
{

    public TransferScheduler(Scheduler scheduler) {
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
