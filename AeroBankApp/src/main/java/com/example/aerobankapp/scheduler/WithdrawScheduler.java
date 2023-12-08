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
    public WithdrawScheduler(@Qualifier("withdraw2") Withdraw withdraw, SchedulerCriteria schedulerCriteria)
    {
        super(schedulerCriteria);
        this.withdraw = withdraw;
        this.withdrawJobDetail = new WithdrawJobDetail(withdraw);
    }


    protected Scheduler getDailySimpleScheduler()
    {
        return null;
    }


    protected Scheduler getWeeklySimpleScheduler() {
        return null;
    }


    protected Scheduler getBiWeeklySimpleScheduler() {
        return null;
    }


    protected Scheduler getWeeklyCronScheduler() {
        return null;
    }


    protected Scheduler getMonthlyCronScheduler() {
        return null;
    }

    protected Scheduler getCustomCronScheduler() throws SchedulerException {
        return null;
    }

}
