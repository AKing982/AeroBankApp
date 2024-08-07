package com.example.aerobankapp.scheduler;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.List;

public interface SchedulerEngine
{
    void startScheduler() throws SchedulerException, InterruptedException;
    void stopScheduler() throws SchedulerException;
    String scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException;
    void updateJobSchedule(String jobName, String groupName, Trigger trigger);
    Boolean pauseJob(String jobName, String groupName) throws SchedulerException;
    Boolean resumeJob(String jobName, String groupName) throws SchedulerException;
    Boolean deleteJob(String jobName, String groupName) throws SchedulerException;
    JobDetail getJobDetails(String job, String group) throws SchedulerException;
    List<JobDetail> listJobs() throws SchedulerException;
    void triggerJob(String job, String group) throws SchedulerException;
    boolean checkJobExists(String job, String group) throws SchedulerException;
    boolean isJobRunning(String job, String group) throws SchedulerException;
}
