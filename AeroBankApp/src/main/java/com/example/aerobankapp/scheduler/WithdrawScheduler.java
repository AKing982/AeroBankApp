package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.WithdrawJobDetail;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.AllArgsConstructor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WithdrawScheduler extends SchedulerEngineBase
{
    private Withdraw withdraw;
    private WithdrawJobDetail withdrawJobDetail;
    private AeroLogger aeroLogger = new AeroLogger(WithdrawScheduler.class);

    @Autowired
    public WithdrawScheduler(Scheduler scheduler, @Qualifier("withdraw") Withdraw withdraw, SchedulerCriteria schedulerCriteria, WithdrawJobDetail withdrawJobDetail)
    {
        super(scheduler, schedulerCriteria);
        this.withdraw = withdraw;
        this.withdrawJobDetail = withdrawJobDetail;
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
    protected Scheduler getCustomCronScheduler() throws SchedulerException {
        return null;
    }

}
