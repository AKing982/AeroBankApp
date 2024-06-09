package com.example.aerobankapp.scheduler.jobs;

import com.example.aerobankapp.workbench.processor.LatePaymentProcessor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LatePaymentJob implements Job
{
    private Logger LOGGER = LoggerFactory.getLogger(LatePaymentJob.class);
    private LatePaymentProcessor latePaymentProcessor;

    @Autowired
    public LatePaymentJob(LatePaymentProcessor latePaymentProcessor){
        this.latePaymentProcessor = latePaymentProcessor;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Executing LatePaymentJob");
        latePaymentProcessor.processLatePayments();
        LOGGER.info("Finished Processing late payments.");
    }
}
