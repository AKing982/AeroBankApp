package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.jobdetail.PurchaseJobDetail;
import com.example.aerobankapp.workbench.transactions.Purchase;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseScheduler
{
    private Purchase purchase;
    private PurchaseJobDetail purchaseJobDetail;

    @Autowired
    public PurchaseScheduler(Purchase purchase)
    {
        this.purchase = purchase;
    }


    protected Scheduler getDailySimpleScheduler() {
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
