package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.configuration.QuartzConfig;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.entity.SchedulerSecurityEntity;
import com.example.aerobankapp.scheduler.security.SchedulerSecurityImpl;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import lombok.Setter;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Getter
@Setter
public abstract class SchedulerEngineBase
{
    protected Scheduler scheduler;
    protected SchedulerCriteria schedulerCriteria;
    private SchedulerSecurityImpl schedulerSecurity;
    protected boolean isStarted;
    protected boolean isInStandby;
    protected boolean isShutdown;
    protected boolean isScheduling;
    protected boolean isTriggerJob;
    protected boolean isStartedWithDelay;
    private long startTime;
    private long endTime;
    private long elapsedTime;
    private TransactionBase transactionBase;
    private AnnotationConfigApplicationContext applicationContext;
    private AeroLogger aeroLogger = new AeroLogger(SchedulerEngineBase.class);

    @Autowired
    private UserProfile userProfile;

    public SchedulerEngineBase(SchedulerCriteria schedulerCriteria)
    {
        this.schedulerCriteria = schedulerCriteria;
    }

    void initializeSecurity()
    {
        schedulerSecurity = new SchedulerSecurityImpl();
    }

    private void nullCheck(Scheduler scheduler)
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

    protected abstract Scheduler getDailySimpleScheduler();
    protected abstract Scheduler getWeeklySimpleScheduler();
    protected abstract Scheduler getBiWeeklySimpleScheduler();
    protected abstract Scheduler getWeeklyCronScheduler();
    protected abstract Scheduler getMonthlyCronScheduler();
    protected abstract Scheduler getCustomCronScheduler() throws SchedulerException;

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
        return false;
    }

    private boolean hasScheduleUserRights()
    {
        return false;
    }

    private boolean hasScheduleAdminRights()
    {
        return false;
    }

    private String getSchedulerUserName()
    {
        return " ";
    }


    public void pause()
    {
        try
        {
            if(hasScheduleAdminRights() || hasScheduleUserRights())
            {
                if(!scheduler.isInStandbyMode())
                {
                    if(!isInStandby())
                    {
                        scheduler.isInStandbyMode();
                    }
                }
            }
            else
            {
                throw new SecurityException("User: " + getSchedulerUserName() + " is not authorized.");
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
            if(hasScheduleUserRights() || hasScheduleAdminRights())
            {
                if(scheduler.isInStandbyMode())
                {
                    if(!isStarted())
                    {
                        startTime = System.currentTimeMillis();
                        scheduler.startDelayed(delay);
                    }

                }
            }
            else
            {
                throw new SecurityException("User: " + getSchedulerUserName() + " is not authorized.");
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

    public void resume()
    {
        try
        {
            if(hasScheduleAdminRights() || hasScheduleUserRights())
            {
                if(scheduler.isInStandbyMode())
                {
                    if(!isStarted())
                    {
                        scheduler.start();
                    }
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
            if(hasScheduleUserRights() || hasScheduleAdminRights())
            {
                if(!scheduler.isShutdown())
                {
                    if(!isShutdown() && !isShutdownParam)
                    {
                        scheduler.shutdown(isShutdown);
                    }
                }
            }

        }catch(SchedulerException ex)
        {
            aeroLogger.error("Unable to shutdown the scheduler: ", ex);
        }
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



    public void scheduleJob(final JobDetail jobDetail, final Trigger trigger)
    {
        try
        {
            if(!isScheduling && isTriggerJob(trigger))
            {
                if(isStarted())
                {
                    scheduler.scheduleJob(jobDetail, trigger);
                    isScheduling = true;
                }
            }

        }catch(SchedulerException ex)
        {
            ex.printStackTrace();
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
                    scheduler.scheduleJob(jobDetail, cronTrigger);
                    isScheduling = true;
                }
            }

        }catch(SchedulerException ex)
        {
            ex.printStackTrace();
        }
    }
}
