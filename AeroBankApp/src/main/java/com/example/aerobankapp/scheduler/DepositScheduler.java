package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.jobdetail.DepositJobDetail;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

@Service
public class DepositScheduler extends SchedulerEngineBase
{
    private DepositJobDetail depositJobDetail;
    private Deposit deposit;

    public DepositScheduler() {
        super();
    }

    @Override
    protected Scheduler getDailySimpleScheduler() {
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
    protected Scheduler getCustomCronScheduler() throws SchedulerException {
        return null;
    }


}
