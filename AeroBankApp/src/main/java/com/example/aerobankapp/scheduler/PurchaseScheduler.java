package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.PurchaseJobDetail;
import com.example.aerobankapp.workbench.transactions.Purchase;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PurchaseScheduler extends SchedulerEngineBase
{
    private Purchase purchase;
    private PurchaseJobDetail purchaseJobDetail;

    @Autowired
    public PurchaseScheduler(Scheduler scheduler, Purchase purchase, SchedulerCriteria schedulerCriteria, PurchaseJobDetail purchaseJobDetail)
    {
        super(scheduler, schedulerCriteria);
        this.purchase = purchase;
        this.purchaseJobDetail = purchaseJobDetail;

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
