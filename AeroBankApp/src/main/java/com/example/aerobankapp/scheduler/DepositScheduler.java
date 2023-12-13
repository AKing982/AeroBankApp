package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.DepositJobDetail;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

@Component
@Getter
public class DepositScheduler extends SchedulerEngineBase
{
    private DepositJobDetail depositJobDetail;
    private Deposit deposit;
    private JobDetail jobDetail;
    private final AeroLogger aeroLogger = new AeroLogger(DepositScheduler.class);

    @Autowired
    public DepositScheduler(@Qualifier("deposit2") Deposit deposit, SchedulerCriteria schedulerCriteria)
    {
        super(schedulerCriteria);
        this.deposit = deposit;
        this.depositJobDetail = new DepositJobDetail(deposit);
    }

    public JobDetail getJobDetail()
    {
        this.jobDetail = depositJobDetail.getJobDetail();
        return jobDetail;
    }

    public void nullCheck()
    {

    }

    @Override
    protected Scheduler getDailySimpleScheduler()
    {
       return null;
    }

    @Override
    protected Scheduler getWeeklySimpleScheduler() {
        return null;
    }

    @Override
    protected Scheduler getBiWeeklySimpleScheduler() {
        return null;
    }

    @Override
    protected Scheduler getWeeklyCronScheduler() {
        return null;
    }

    @Override
    protected Scheduler getMonthlyCronScheduler() {
        return null;
    }

    @Override
    protected Scheduler getCustomCronScheduler() throws SchedulerException
    {
        return null;
    }


}
