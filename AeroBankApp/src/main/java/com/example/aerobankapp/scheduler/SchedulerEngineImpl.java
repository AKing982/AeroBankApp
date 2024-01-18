package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.JobDetailBase;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

public class SchedulerEngineImpl<T extends TransactionBase, S extends JobDetailBase> extends SchedulerEngineBase
{
    private T transactionType;
    private JobDetail jobDetail;
    private Trigger trigger;
    private CronTrigger cronTrigger;

    public SchedulerEngineImpl(T transaction, SchedulerCriteria schedulerCriteria, ScheduleType scheduleType) {
        super(schedulerCriteria, scheduleType);
        this.transactionType = transaction;
        initializeTransaction(transaction);
    }

    private void initializeTransaction(T transactionType)
    {
        if(transactionType instanceof Deposit)
        {
            this.transactionType = (T) transactionType;
        }
        else if(transactionType instanceof Withdraw)
        {
            this.transactionType = (T) transactionType;
        }
        else if(transactionType instanceof Purchase)
        {
            this.transactionType = (T) transactionType;
        }
        else if(transactionType instanceof Transfer)
        {
            this.transactionType = (T) transactionType;
        }
    }


    @Override
    protected Scheduler getSchedulerFactoryInstance()
    {
        ScheduleType scheduleType1 = super.getScheduleType();
        return schedulerFactoryProducer.getSchedulerFactory(scheduleType1);
    }
}
