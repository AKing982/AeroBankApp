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
    private final Scheduler scheduler;
    private final int MAX_RETRIES = 5;
    private Logger LOGGER = LoggerFactory.getLogger(SchedulerEngineImpl.class);

    @Autowired
    public SchedulerEngineImpl(Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }

    @Override
    public void startScheduler() throws SchedulerException, InterruptedException
    {
        int attempts = 0;
        while(!scheduler.isStarted() && attempts < MAX_RETRIES)
        {
            try
            {
                scheduler.start();
                LOGGER.info("Scheduler started successfully");
                return;

            }catch(Exception se)
            {
                attempts++;
                LOGGER.warn("Failed to start scheduler, retrying...", se);
                if(attempts >= MAX_RETRIES)
                {
                    throw new SchedulerException("Failed to start scheduler after " + MAX_RETRIES, se);
                }
                Thread.sleep(1000);
            }
        }
        LOGGER.info("Scheduler is already started");
    }

    @Override
    public void stopScheduler() throws SchedulerException {
        do
        {
            try
            {
                scheduler.shutdown(true);
                LOGGER.info("Scheduler stopped successfully");
                return;

            }catch(SchedulerException se)
            {
                LOGGER.warn("Failed to stop scheduler", se);
            }

        }while(scheduler.isStarted());
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
    public Boolean pauseJob(String jobName, String groupName) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, groupName);
        try
        {
            if(scheduler.checkExists(jobKey))
            {
                scheduler.pauseJob(jobKey);
                return true;
            }
            else
            {
                LOGGER.warn("Attempt to pause non existent job: {}", jobKey);
                return true;
            }
        }catch(SchedulerException ex)
        {
            LOGGER.error("An exception has occurred pausing Job: ", ex);
            return false;
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Boolean resumeJob(String jobName, String groupName) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, groupName);
        int maxAttempts = 5;
        int attempts = 0;
        while(attempts < maxAttempts)
        {
            try
            {
                if(scheduler.isInStandbyMode())
                {
                    LOGGER.warn("Scheduler is in standby mode. Waiting before attempting to resume job.");
                    Thread.sleep(1000);
                    attempts++;
                }
                else
                {
                    if(scheduler.checkExists(jobKey))
                    {
                        scheduler.resumeJob(jobKey);
                        LOGGER.info("Job Resumed Successfully: {}", jobKey);
                        return true;
                    }
                    else
                    {
                        LOGGER.warn("Job Does Not Exist: {}", jobKey);
                        return false;
                    }
                }

            }catch(SchedulerException e)
            {
                LOGGER.error("An exception has occurred resuming Job: {} ", jobKey, e);
                throw e;

            }catch(InterruptedException ie)
            {
                LOGGER.error("An exception has occurred resuming Job: {}", jobKey, ie);
                Thread.currentThread().interrupt();
                return false;
            }
        }
        LOGGER.error("Failed to resume job after {} attempts: {}", maxAttempts, attempts);
        return false;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Boolean deleteJob(String jobName, String groupName)
    {
        JobKey jobKey = new JobKey(jobName, groupName);
        try
        {
            if(scheduler.checkExists(jobKey))
            {
                boolean deleted = scheduler.deleteJob(jobKey);
                if(deleted)
                {
                    LOGGER.info("Job deleted successfully");
                }
                else
                {
                    LOGGER.warn("Failed to delete Job: {}", jobKey);
                }
                return deleted;
            }
            else
            {
                LOGGER.warn("Job does not exist: {}", jobKey);
                return false;
            }
        }catch(SchedulerException e)
        {
            LOGGER.warn("Job Does not exist: {}", jobKey);
            return false;
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


