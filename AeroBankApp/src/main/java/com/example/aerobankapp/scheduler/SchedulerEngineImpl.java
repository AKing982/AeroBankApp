package com.example.aerobankapp.scheduler;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void pauseJob(String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        try
        {
            scheduler.pauseJob(jobKey);

        }catch(SchedulerException e)
        {
            LOGGER.error("An exception has occurred pausing Job: ", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void resumeJob(String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        try
        {
            scheduler.resumeJob(jobKey);

        }catch(SchedulerException e)
        {
            LOGGER.error("An exception has occurred while resuming job: ", e);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteJob(String jobName, String groupName)
    {
        JobKey jobKey = new JobKey(jobName, groupName);
        try
        {
            scheduler.deleteJob(jobKey);

        }catch(SchedulerException ex)
        {
            LOGGER.error("An exception has ocurred while deleting a job: ", ex);
        }
    }

    @Override
    public JobDetail getJobDetails(String job, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(job, group);
        return scheduler.getJobDetail(jobKey);
    }

    @Override
    public List<JobDetail> listJobs() throws SchedulerException {
        List<JobDetail> jobDetails = new ArrayList<>();
        for(String groupName : scheduler.getJobGroupNames())
        {
            for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName)))
            {
                jobDetails.add(scheduler.getJobDetail(jobKey));
            }
        }
        return jobDetails;
    }

    @Override
    public void triggerJob(String job, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(job, group);
        scheduler.triggerJob(jobKey);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public boolean checkJobExists(String job, String group) throws SchedulerException {
        return scheduler.checkExists(new JobKey(job, group));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public boolean isJobRunning(String job, String group) throws SchedulerException {
        List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
        for (JobExecutionContext jobCtx : currentlyExecutingJobs) {
            if (jobCtx.getJobDetail().getKey().getName().equals(job) &&
                    jobCtx.getJobDetail().getKey().getGroup().equals(group)) {
                return true;
            }
        }
        return false;
    }
}
