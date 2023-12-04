package com.example.aerobankapp.scheduler.jobdetail;

import org.quartz.JobDetail;

public abstract class JobDetailBase
{
    private String jobName;
    private String description;

    public abstract JobDetail getJobDetail();
}
