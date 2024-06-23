package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.exceptions.IllegalDateException;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.scheduler.SchedulerEngine;
import com.example.aerobankapp.scheduler.jobs.BillPaymentJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class BillPaymentJobManager {
    private final SchedulerEngine schedulerEngine;
    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentJobManager.class);

    @Autowired
    public BillPaymentJobManager(SchedulerEngine schedulerEngine) {
        this.schedulerEngine = schedulerEngine;
    }

    public Boolean cancelPayment(String jobId) {
        return null;
    }

    public Trigger createBillPaymentTrigger(final Date scheduleDate) {
        return TriggerBuilder.newTrigger()
                .withIdentity("paymentJob", "group1")
                .startAt(scheduleDate)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();
    }

    public JobDetail createBillPaymentJobDetail(String uniqueId, Long paymentId, Long schedulePaymentId) {
        return JobBuilder.newJob(BillPaymentJob.class)
                .usingJobData("billPaymentId", paymentId)
                .usingJobData("billPaymentScheduleId", schedulePaymentId)
                .withIdentity("paymentJob_" + uniqueId, "group1").build();
    }

    public ZonedDateTime createBillPaymentZonedDateTime(LocalDate scheduledPaymentDate) {
        if(scheduledPaymentDate == null){
            throw new IllegalDateException("Scheduled payment date cannot be null");
        }
        return scheduledPaymentDate.atStartOfDay(ZoneId.systemDefault());
    }

    public String generateUniqueId(){
        return String.valueOf(System.currentTimeMillis() % 1000000);
    }

    public Date createScheduleDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public boolean executeScheduleJobTask(BillPayment billPayment)
    {
        String uniqueId = generateUniqueId();
        JobDetail job = createBillPaymentJobDetail(uniqueId, billPayment.getPaymentID(), billPayment.getSchedulePaymentID());
        LocalDate scheduledPaymentDate = billPayment.getScheduledPaymentDate();

        ZonedDateTime zonedDateTime = createBillPaymentZonedDateTime(scheduledPaymentDate);
        Date scheduledDate = createScheduleDate(zonedDateTime);

        Trigger trigger = createBillPaymentTrigger(scheduledDate);

        try
        {
            schedulerEngine.startScheduler();
            schedulerEngine.scheduleJob(job, trigger);
            return true;

        }catch(SchedulerException e)
        {
            LOGGER.error("Error occurred while scheduling payment job", e);
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
