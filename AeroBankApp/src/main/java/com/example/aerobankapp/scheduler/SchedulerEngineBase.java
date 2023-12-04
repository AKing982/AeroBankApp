package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.jobdetail.JobDetailBase;
import org.quartz.*;

public abstract class SchedulerEngineBase
{
    protected Scheduler scheduler;
    protected JobDetail jobDetail;
    protected Trigger trigger;
    protected CronTrigger cronTrigger;

    public SchedulerEngineBase()
    {

    }


    private void nullCheck(Scheduler scheduler)
    {

    }

    protected abstract Scheduler getDailySimpleScheduler();
    protected abstract Scheduler getWeeklySimpleScheduler();
    protected abstract Scheduler getBiWeeklySimpleScheduler();
    protected abstract Scheduler getWeeklyCronScheduler();
    protected abstract Scheduler getMonthlyCronScheduler();
    protected abstract Scheduler getCustomCronScheduler() throws SchedulerException;

    public void scheduleJob()
    {
        try
        {
            scheduler.scheduleJob(jobDetail, trigger);

        }catch(SchedulerException ex)
        {
            ex.printStackTrace();
        }
    }
}
