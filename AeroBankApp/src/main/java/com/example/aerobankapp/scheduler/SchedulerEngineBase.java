package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.configuration.QuartzConfig;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.JobDetailBase;
import com.example.aerobankapp.scheduler.security.SchedulerSecurity;
import lombok.Getter;
import lombok.Setter;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Getter
@Setter
public abstract class SchedulerEngineBase
{
    protected Scheduler scheduler;
    protected SchedulerCriteria schedulerCriteria;
    private SchedulerSecurity schedulerSecurity;
    private AnnotationConfigApplicationContext applicationContext;

    public SchedulerEngineBase(Scheduler scheduler, SchedulerCriteria schedulerCriteria)
    {
        this.scheduler = scheduler;
        this.schedulerCriteria = schedulerCriteria;
        initializeContext();
    }

    public void initializeContext()
    {
        applicationContext = new AnnotationConfigApplicationContext(QuartzConfig.class);
    }

    private void nullCheck(Scheduler scheduler)
    {

    }

    private Scheduler getSchedulerBean()
    {
        return getApplicationContext().getBean(Scheduler.class);
    }

    protected abstract Scheduler getDailySimpleScheduler();
    protected abstract Scheduler getWeeklySimpleScheduler();
    protected abstract Scheduler getBiWeeklySimpleScheduler();
    protected abstract Scheduler getWeeklyCronScheduler();
    protected abstract Scheduler getMonthlyCronScheduler();
    protected abstract Scheduler getCustomCronScheduler() throws SchedulerException;

    protected Scheduler getScheduler()
    {
        return getSchedulerBean();
    }

    public void scheduleJob(JobDetail jobDetail, Trigger trigger)
    {
        try
        {
            scheduler.scheduleJob(jobDetail, trigger);

        }catch(SchedulerException ex)
        {
            ex.printStackTrace();
        }
    }

    public void scheduleCronJob(JobDetail jobDetail, CronTrigger cronTrigger)
    {
        try
        {
            scheduler.scheduleJob(jobDetail, cronTrigger);

        }catch(SchedulerException ex)
        {
            ex.printStackTrace();
        }
    }
}
