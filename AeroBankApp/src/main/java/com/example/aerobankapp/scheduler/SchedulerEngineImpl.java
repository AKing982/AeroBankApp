package com.example.aerobankapp.scheduler;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
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
        return null;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void updateJobSchedule(String jobName, String groupName, Trigger trigger) {

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
