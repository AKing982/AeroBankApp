package com.example.aerobankapp.scheduler.jobs;

import com.example.aerobankapp.model.BillPaymentIdCriteria;
import com.example.aerobankapp.workbench.runner.BillPaymentRunner;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@DisallowConcurrentExecution
public class BillPaymentJob implements Job, ApplicationContextAware
{
    private BillPaymentRunner billPaymentRunner;
    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String paymentId = jobExecutionContext.getJobDetail().getJobDataMap().getString("billPaymentId");
        String scheduleId = jobExecutionContext.getJobDetail().getJobDataMap().getString("billPaymentScheduleId");
        Long paymentIdAsLong = Long.parseLong(paymentId);
        Long scheduleIdAsLong = Long.parseLong(scheduleId);
        BillPaymentIdCriteria billPaymentIdCriteria = new BillPaymentIdCriteria(paymentIdAsLong, scheduleIdAsLong);
        try
        {
            billPaymentRunner.setBillPaymentIdCriteria(billPaymentIdCriteria);
            billPaymentRunner.run();

        }catch(Exception e)
        {
            LOGGER.error("There was an error running the bill payment process", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.billPaymentRunner = applicationContext.getBean(BillPaymentRunner.class);
    }
}
