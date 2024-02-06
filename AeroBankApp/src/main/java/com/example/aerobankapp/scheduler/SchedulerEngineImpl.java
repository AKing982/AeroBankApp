package com.example.aerobankapp.scheduler;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerEngineImpl implements SchedulerEngine
{
    private final CronExpressionBuilder cronExpressionBuilder;
    private final Scheduler scheduler;
    private Logger LOGGER = LoggerFactory.getLogger(SchedulerEngineImpl.class);

    @Autowired
    public SchedulerEngineImpl(Scheduler scheduler, CronExpressionBuilder cronExpressionBuilder)
    {
        this.cronExpressionBuilder = cronExpressionBuilder;
        this.scheduler = scheduler;
    }

    public String getCronExpression()
    {
        return cronExpressionBuilder.getCronExpression();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        scheduler.scheduleJob(jobDetail, trigger);
        return trigger.getKey().toString();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void updateJobSchedule(String jobName, String groupName, Trigger trigger)
    {
        JobKey jobKey = new JobKey(jobName, groupName);
        try
        {
            if(scheduler.checkExists(jobKey))
            {
                scheduler.rescheduleJob(TriggerKey.triggerKey(jobName, groupName), trigger);
            }
        }catch(SchedulerException e)
        {
            LOGGER.error("An exception has occurred updating Job Schedule: ", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void pauseJob(String jobName, String groupName) throws SchedulerException {

    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void resumeJob(String jobName, String groupName) throws SchedulerException {

    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteJob(String jobName, String groupName) throws SchedulerException {

    }

    @Override
    public JobDetail getJobDetails(String job, String group) throws SchedulerException {
        return null;
    }

    @Override
    public List<JobDetail> listJobs() throws SchedulerException {
        return null;
    }

    @Override
    public void triggerJob(String job, String group) throws SchedulerException {

    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public boolean checkJobExists(String job, String group) throws SchedulerException {
        return false;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public boolean isJobRunning(String job, String group) throws SchedulerException {
        return false;
    }
}
