package com.example.aerobankapp.scheduler.jobs;

import com.example.aerobankapp.converter.BillPaymentConverter;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.services.BillPaymentService;
import com.example.aerobankapp.workbench.data.BillPaymentDataManager;
import com.example.aerobankapp.workbench.processor.BillPaymentProcessor;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;

@DisallowConcurrentExecution
public class BillPaymentJob implements Job, ApplicationContextAware
{
    private BillPaymentProcessor billPaymentProcessor;
    private BillPaymentDataManager billPaymentDataManager;
    private BillPaymentConverter billPaymentConverter;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String paymentId = jobExecutionContext.getJobDetail().getJobDataMap().getString("billPaymentId");
        String scheduleId = jobExecutionContext.getJobDetail().getJobDataMap().getString("billPaymentScheduleId");
        Long id = Long.valueOf(paymentId);
        Long sId = Long.valueOf(scheduleId);
        Optional<BillPaymentEntity> paymentEntity = billPaymentDataManager.findBillPaymentByID(id);
        Optional<BillPaymentScheduleEntity> billPaymentSchedule = billPaymentDataManager.findBillPaymentScheduleByID(sId);
        if(paymentEntity.isPresent() && billPaymentSchedule.isPresent()) {
            BillPayment payment = billPaymentConverter.convert(paymentEntity.get(), billPaymentSchedule.get());
            billPaymentProcessor.processSinglePayment(payment);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.billPaymentProcessor = applicationContext.getBean(BillPaymentProcessor.class);
        this.billPaymentDataManager = applicationContext.getBean(BillPaymentDataManager.class);
        this.billPaymentConverter = applicationContext.getBean(BillPaymentConverter.class);
    }
}
