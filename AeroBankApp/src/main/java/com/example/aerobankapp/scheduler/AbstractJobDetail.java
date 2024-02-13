package com.example.aerobankapp.scheduler;

import org.quartz.JobDetail;

public abstract class AbstractJobDetail {
    protected static int jobCounter = 1;
    protected static int groupCounter = 1;

    protected String jobName;

    protected String description;

    public AbstractJobDetail(String description)
    {
        this.description = description;
    }

    public abstract JobDetail getJobDetail();
}
