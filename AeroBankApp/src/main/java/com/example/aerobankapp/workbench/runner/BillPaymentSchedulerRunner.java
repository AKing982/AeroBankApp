package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.converter.BillPaymentConverter;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.workbench.scheduler.BillPaymentScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Deprecated
public class BillPaymentSchedulerRunner implements Runnable {

    private final BillPaymentScheduler billPaymentScheduler;
    private final Queue<BillPaymentEntity> billPaymentQueue;
    private BillPaymentConverter billPaymentConverter;

    @Autowired
    public BillPaymentSchedulerRunner(BillPaymentScheduler billPaymentScheduler,
                                      BillPaymentConverter billPaymentConverter) {
        this.billPaymentScheduler = billPaymentScheduler;
        this.billPaymentQueue = new LinkedBlockingQueue<>();
        this.billPaymentConverter = billPaymentConverter;
    }

    public TreeMap<String, LocalDate> createNextPaymentDetails(BillPayment billPayment)
    {
        return billPaymentScheduler.getNextPaymentDetails(billPayment);
    }

    public void scheduleSinglePayment(BillPayment billPayment)
    {
        billPaymentScheduler.schedulePayment(billPayment);
    }

    public void scheduleBatchPayments(List<BillPayment> billPaymentList)
    {
        for(BillPayment billPayment : billPaymentList) {
            if(billPayment != null)
            {
                billPaymentScheduler.schedulePayment(billPayment);
            }
        }
    }

    public boolean rescheduleBillPayment(String jobId, Date newDate)
    {
        return billPaymentScheduler.reschedulePayment(jobId, newDate);
    }

    public boolean cancelExistingPayment(String jobId){
      //  return billPaymentScheduler.cancelPayment(jobId);
        return false;
    }

    public Optional<LocalDate> getNextPaymentDate(BillPayment billPayment)
    {
        return billPaymentScheduler.getNextPaymentDate(billPayment);
    }

    public Optional<LocalDate> getPreviousPaymentDate(BillPayment billPayment)
    {
        return billPaymentScheduler.getPreviousPaymentDate(billPayment);
    }

    public Collection<BillPaymentEntity> fetchBillPaymentsFromDatabase()
    {
        return billPaymentScheduler.getBillPaymentsFromDB();
    }

    public Collection<BillPaymentScheduleEntity> fetchBillPaymentSchedulesFromDatabase()
    {
        return billPaymentScheduler.getBillPaymentSchedulesFromDB();
    }

    public List<BillPayment> getBillPaymentEntityConvertedToBillPaymentModel(Collection<BillPaymentEntity> billPaymentEntities, Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities)
    {
        List<BillPayment> billPayments = new ArrayList<>();
        if(billPaymentEntities == null || billPaymentScheduleEntities == null)
        {
            throw new IllegalStateException("billPaymentEntities or billPaymentScheduleEntities are null");
        }

        for(BillPaymentEntity billPaymentEntity : billPaymentEntities)
        {
            for(BillPaymentScheduleEntity billPaymentScheduleEntity : billPaymentScheduleEntities)
            {
                if(billPaymentEntity != null && billPaymentScheduleEntity != null)
                {
                    BillPayment billPayment = billPaymentConverter.convert(billPaymentEntity, billPaymentScheduleEntity);
                    billPayments.add(billPayment);
                }
            }
        }

        return billPayments;
    }

    @Override
    public void run()
    {
        Collection<BillPaymentEntity> billPaymentEntities = fetchBillPaymentsFromDatabase();
        Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities = fetchBillPaymentSchedulesFromDatabase();

        List<BillPayment> billPayments = getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);
        scheduleBatchPayments(billPayments);
    }
}
