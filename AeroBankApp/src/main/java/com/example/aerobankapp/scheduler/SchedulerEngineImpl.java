package com.example.aerobankapp.scheduler;


import jakarta.validation.constraints.NotNull;
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
import java.util.UUID;

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
    public void startScheduler() throws SchedulerException {
        if(!scheduler.isStarted())
        {
            scheduler.start();
        }
    }

    @Override
    public void stopScheduler() throws SchedulerException {
        if(!scheduler.isShutdown())
        {
            scheduler.shutdown();
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        if (scheduler.checkExists(jobDetail.getKey()))
        {
            // Append a UUID to the job name to make it unique
            JobDetail uniqueJobDetail = getUniqueJobDetail(jobDetail);

            // Schedule the new job
            scheduler.scheduleJob(uniqueJobDetail, trigger);
            return trigger.getKey().toString();
        }
        else
        {
            scheduler.scheduleJob(jobDetail, trigger);
            return trigger.getKey().toString();
        }
    }

    private static JobDetail getUniqueJobDetail(JobDetail jobDetail) {
        String uniqueName = generateUniqueName(jobDetail);
        JobKey uniqueJobKey = getNewJobKey(jobDetail, uniqueName);

        // Create a new JobDetail with the unique key
        return getNewJobDetail(jobDetail, uniqueJobKey);
    }

    private static JobDetail getNewJobDetail(JobDetail jobDetail, JobKey uniqueJobKey) {
        return JobBuilder.newJob(jobDetail.getJobClass())
                .withIdentity(uniqueJobKey)
                .build();
    }

    @NotNull
    private static JobKey getNewJobKey(JobDetail jobDetail, String uniqueName) {
        return JobKey.jobKey(uniqueName, jobDetail.getKey().getGroup());
    }

    @NotNull
    private static String generateUniqueName(JobDetail jobDetail) {
        return jobDetail.getKey().getName() + "-" + UUID.randomUUID().toString();
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
    @PreAuthorize("hasRole('ADMIN')")
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


