package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.DepositJobDetail;
import com.example.aerobankapp.workbench.transactions.Deposit;
import lombok.Getter;
import lombok.Setter;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;

@Getter
@Setter
public class DepositScheduler extends SchedulerEngineBase implements Runnable
{
    private DepositJobDetail depositJobDetail;
    private Trigger trigger;
    private JobDetail jobDetail;
    private Deposit deposit;

    public DepositScheduler(@Qualifier("deposit") Deposit deposit, SchedulerCriteria schedulerCriteria, ScheduleType scheduleType)
    {
        super(schedulerCriteria, scheduleType);
        this.depositJobDetail = new DepositJobDetail(deposit);
        this.jobDetail = depositJobDetail.getJobDetail();
    }

    @Override
    protected Scheduler getSchedulerFactoryInstance()
    {
        ScheduleType scheduleType1 = super.getScheduleType();
        return super.schedulerFactoryProducer.getSchedulerFactory(scheduleType1);
    }

    @Override
    @Async
    public void run()
    {
        try
        {
            Scheduler scheduler1 = getSchedulerFactoryInstance();
            scheduler1.scheduleJob(getJobDetail(), getTrigger());

            Thread.sleep(5000);

        }catch(InterruptedException | SchedulerException ex)
        {

        }
    }
}
