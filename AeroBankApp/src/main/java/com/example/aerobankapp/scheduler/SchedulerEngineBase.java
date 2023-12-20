package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.configuration.QuartzConfig;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.factory.SchedulerFactoryProducer;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import lombok.Setter;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public abstract class SchedulerEngineBase
{
    protected Scheduler scheduler;
    protected SchedulerCriteria schedulerCriteria;
    protected boolean isStarted;
    protected boolean isInStandby;
    protected boolean isShutdown;
    protected boolean isScheduling;
    protected boolean isTriggerJob;
    protected boolean isStartedWithDelay;
    private long startTime;
    private long endTime;
    private long elapsedTime;
    private ScheduleCriteriaParser scheduleCriteriaParser;
    protected List<JobExecutionContext> currentlyExecutingJobs;
    private TransactionBase transactionBase;
    private AnnotationConfigApplicationContext applicationContext;
    protected ScheduleType scheduleType;
    protected SchedulerFactoryProducer schedulerFactoryProducer;
    private AeroLogger aeroLogger = new AeroLogger(SchedulerEngineBase.class);

    public SchedulerEngineBase(SchedulerCriteria schedulerCriteria, ScheduleType scheduleType)
    {
        this.schedulerCriteria = schedulerCriteria;
        this.scheduleType = scheduleType;
    }

    void initializeSecurity()
    {

    }

    private AnnotationConfigApplicationContext getContext()
    {
        applicationContext = new AnnotationConfigApplicationContext(QuartzConfig.class);
        return applicationContext;
    }

    public Scheduler getSchedulerBean() throws SchedulerException {
        if(scheduler == null)
        {
            this.scheduler = (Scheduler) getContext().getBean("scheduler");
        }
        return scheduler;
    }

    protected abstract Scheduler getSchedulerFactoryInstance();

    protected Scheduler getScheduler() throws SchedulerException {
        return getSchedulerBean();
    }

    public boolean isStarted() throws SchedulerException
    {
        isStarted = getScheduler().isStarted();
        return isStarted;
    }

    public boolean isShutdown() throws SchedulerException
    {
        isShutdown = getScheduler().isShutdown();
        return isShutdown;
    }

    public boolean isInStandby() throws SchedulerException
    {
        isInStandby = getScheduler().isInStandbyMode();
        return isInStandby;
    }

    public boolean isScheduling()
    {
        return isScheduling;
    }

    public void setIsScheduling(boolean isScheduling)
    {
        this.isScheduling = isScheduling;
    }


    public void standBy()
    {
        try
        {
            if(!scheduler.isInStandbyMode())
            {
                if(!isInStandby())
                {
                    scheduler.isInStandbyMode();
                }
            }

        }catch(SchedulerException ex)
        {
            aeroLogger.error("Unable to pause scheduler: ", ex);
        }
    }

    public void resumeWithDelay(int delay)
    {
        try
        {

            if(scheduler.isInStandbyMode())
            {
                if(!isStarted())
                {
                    startTime = System.currentTimeMillis();
                    scheduler.startDelayed(delay);
                    setIsScheduling(true);
                }

            }
        }catch(SchedulerException ex)
        {
            aeroLogger.error("Unable to resume scheduler: ", ex);
        }
        finally
        {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            aeroLogger.info("Scheduler process completed in " + elapsedTime);
        }
    }

    public void resumeAll()
    {

    }

    public void resume()
    {
        try
        {

            if(scheduler.isInStandbyMode())
            {
                if(!isStarted())
                {
                    scheduler.start();

                }
            }

        }catch(SchedulerException ex)
        {
            aeroLogger.error("Unable to Resume the scheduler: ", ex);
        }
    }

    public void shutdown(boolean isShutdown)
    {
        boolean isShutdownParam = isShutdown;
        try
        {
            if(!scheduler.isShutdown())
            {
                if(!isShutdown() && !isShutdownParam)
                {
                    scheduler.shutdown(isShutdown);

                }
            }


        }catch(SchedulerException ex)
        {
            aeroLogger.error("Unable to shutdown the scheduler: ", ex);
        }
    }

    public Set<JobKey> getJobKeys()
    {
        Set<JobKey> jobKeys = new HashSet<>();
        try
        {
            Scheduler scheduler = getScheduler();
            jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());

        }catch(SchedulerException ex)
        {
            aeroLogger.error("Unable to retrieve Job Keys: " + ex);
        }

        return jobKeys;
    }

    public void stop()
    {
        try {
            Scheduler scheduler = getScheduler();
            if (!scheduler.isShutdown())
            {
                if(!isShutdown())
                {
                    scheduler.shutdown();
                }
            }
        } catch (SchedulerException ex)
        {
            aeroLogger.error("Scheduler Shutdown ran into an error: ", ex);
        }
    }

    public boolean checkExists(JobKey jobKey)
    {
        Set<JobKey> jobKeys = getJobKeys();
        return jobKeys.contains(jobKey);
    }

    public List<JobExecutionContext> getCurrentlyExecutingJobs()
    {
        try
        {
            Scheduler scheduler1 = getScheduler();
            currentlyExecutingJobs = scheduler1.getCurrentlyExecutingJobs();

        }catch(SchedulerException ex)
        {

        }
        return currentlyExecutingJobs;
    }

    public Date rescheduleJob(TriggerKey triggerKey, Trigger trigger) throws SchedulerException
    {
        return getScheduler().rescheduleJob(triggerKey, trigger);
    }

    public void addJob(JobDetail jobDetail, boolean param)
    {
        try
        {
            Scheduler scheduler1 = getScheduler();
            scheduler1.addJob(jobDetail, param);

        }catch(SchedulerException ex)
        {
            aeroLogger.error("Unable to add Job with JobDetail: " + jobDetail.toString(), ex);
        }
    }

    public boolean deleteJob(JobKey jobkey) throws SchedulerException
    {
        aeroLogger.info("Deleting Job with key: " + jobkey);
        return getScheduler().deleteJob(jobkey);
    }


    public void start()
    {
        try
        {
            Scheduler scheduler = getScheduler();
            if(!scheduler.isStarted() && scheduler.isShutdown())
            {
                if(!isStarted())
                {
                    scheduler.start();
                }
            }

        }catch(SchedulerException ex)
        {
            aeroLogger.error("Unable to start the scheduler: ", ex);
        }
    }

    public void scheduleJob(final JobDetail jobDetail, final Trigger trigger)
    {
        try
        {
            if(!isScheduling && isTriggerJob(trigger))
            {
                if(isStarted())
                {
                    clearTimer();
                    startTime = System.currentTimeMillis();
                    scheduler.scheduleJob(jobDetail, trigger);
                    isScheduling = true;
                }
            }

        }catch(SchedulerException ex)
        {
            ex.printStackTrace();
        }finally {
            endTime = System.currentTimeMillis();
            elapsedTime = startTime - elapsedTime;
            aeroLogger.info("Total time for scheduling Job: " + elapsedTime);
        }
    }

    public void scheduleJob(final Trigger trigger)
    {
        try
        {
            if(!isScheduling && isTriggerJob(trigger))
            {
                if(isStarted())
                {
                    scheduler.scheduleJob(trigger);
                }
            }

        }catch(SchedulerException ex)
        {

        }
    }

    public boolean isTriggerJob(final Trigger trigger)
    {
        return !(trigger instanceof CronTrigger);
    }

    public void scheduleCronJob(final JobDetail jobDetail, final CronTrigger cronTrigger)
    {
        try
        {
            if(!isScheduling && !isTriggerJob(cronTrigger))
            {
                if(isStarted())
                {
                    clearTimer();
                    startTime = System.currentTimeMillis();
                    scheduler.scheduleJob(jobDetail, cronTrigger);
                    setIsScheduling(true);
                }
            }

        }catch(SchedulerException ex)
        {
            ex.printStackTrace();
        }
        finally {
            endTime = System.currentTimeMillis();
            elapsedTime = startTime - endTime;
            aeroLogger.info("Cron Job scheduled in: " + elapsedTime);
        }
    }

    private void clearTimer()
    {
        startTime = 0;
        endTime = 0;
        elapsedTime = 0;
    }
}
