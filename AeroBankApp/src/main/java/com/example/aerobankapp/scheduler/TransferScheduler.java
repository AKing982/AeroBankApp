package com.example.aerobankapp.scheduler;


import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.workbench.transactions.TransferDTO;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Qualifier;

public class TransferScheduler extends SchedulerEngineBase
{
    private TransferDTO transfer;
    private TransferJobDetail transferJobDetail;

    public TransferScheduler(TransferDTO transfer, SchedulerCriteria schedulerCriteria)
    {
        super(schedulerCriteria);
        this.transfer = transfer;
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
