package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.DepositJobDetail;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class DepositScheduler extends SchedulerEngineBase
{
    private DepositJobDetail depositJobDetail;
    private Deposit deposit;
    private final AeroLogger aeroLogger = new AeroLogger(DepositScheduler.class);

    @Autowired
    public DepositScheduler(Scheduler scheduler, @Qualifier("deposit") Deposit deposit, SchedulerCriteria schedulerCriteria, DepositJobDetail jobDetail) {
        super(scheduler, schedulerCriteria);
        this.deposit = deposit;
        this.depositJobDetail = jobDetail;
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
