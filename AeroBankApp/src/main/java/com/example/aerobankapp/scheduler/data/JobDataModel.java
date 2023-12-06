package com.example.aerobankapp.scheduler.data;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.quartz.JobDataMap;

public interface JobDataModel
{
    JobDataMap getJobDataMap();
}
