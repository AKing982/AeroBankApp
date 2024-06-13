package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.scheduler.BillPaymentScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillPaymentSchedulerRunner implements Runnable
{
    private final BillPaymentScheduler billPaymentScheduler;

    @Autowired
    public BillPaymentSchedulerRunner(BillPaymentScheduler billPaymentScheduler)
    {
        this.billPaymentScheduler = billPaymentScheduler;
    }

    @Override
    public void run() {

    }
}
