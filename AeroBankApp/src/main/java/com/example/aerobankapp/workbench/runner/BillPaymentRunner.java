package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.converter.BillPaymentConverter;
import com.example.aerobankapp.engine.BillPaymentEngine;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.BillPaymentSchedule;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import com.example.aerobankapp.workbench.data.BillPaymentDataManager;
import com.example.aerobankapp.workbench.data.BillPaymentDataManagerImpl;
import com.example.aerobankapp.workbench.processor.BillPaymentProcessor;
import com.example.aerobankapp.workbench.queues.BillPaymentQueue;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * This class will manage when the BillPaymentRunner will run and schedule payment tasks
 */
@Component
public class BillPaymentRunner implements Runnable
{
    private final BillPaymentProcessor billPaymentProcessor;
    private final BillPaymentConverter billPaymentConverter;
    private BillPaymentDataManager billPaymentDataManager;
    private RabbitTemplate rabbitTemplate;
    private TreeMap<LocalDate, Collection<BillPayment>> groupedBillPaymentsByDate = new TreeMap<>();

    @Autowired
    public BillPaymentRunner(BillPaymentProcessor billPaymentProcessor,
                             BillPaymentConverter billPaymentConverter,
                             BillPaymentDataManager billPaymentDataManager) {
        this.billPaymentProcessor = billPaymentProcessor;
        this.billPaymentConverter = billPaymentConverter;
        this.billPaymentDataManager = billPaymentDataManager;
    }


    public Collection<BillPaymentEntity> getAllBillPayments()
    {
        Collection<BillPaymentEntity> billPaymentEntities = billPaymentDataManager.findAllBillPayments();
        if(billPaymentEntities == null || billPaymentEntities.isEmpty())
        {
            return Collections.emptyList();
        }
        return billPaymentEntities;
    }

    public Collection<BillPaymentScheduleEntity> getAllBillPaymentSchedules()
    {
        Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities = billPaymentDataManager.findAllBillPaymentSchedules();
        if(billPaymentScheduleEntities == null || billPaymentScheduleEntities.isEmpty())
        {
            return Collections.emptyList();
        }
        return billPaymentScheduleEntities;
    }

    public Collection<BillPayment> getBillPaymentEntitiesConvertedToBillPaymentModel(Collection<BillPaymentEntity> billPaymentEntities, Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities) {
        Collection<BillPayment> billPayments = new ArrayList<>();
        if(billPaymentEntities == null || billPaymentEntities.isEmpty())
        {
            billPaymentEntities = new ArrayList<>();
        }

        if(billPaymentScheduleEntities == null || billPaymentScheduleEntities.isEmpty())
        {
            billPaymentScheduleEntities = new ArrayList<>();
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

    public boolean scheduleAndExecuteAllPayments(final TreeMap<LocalDate, Collection<BillPayment>> billPaymentsByDate)
    {
        if(billPaymentsByDate == null || billPaymentsByDate.isEmpty())
        {
            return false;
        }
        for(Map.Entry<LocalDate, Collection<BillPayment>> entry : billPaymentsByDate.entrySet())
        {
            LocalDate paymentDate = entry.getKey();
            Collection<BillPayment> billPayments = entry.getValue();
            List<BillPayment> billPaymentList = billPayments.stream().toList();

            // Process the payments
            for(BillPayment billPayment : billPaymentList)
            {
                TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment = billPaymentProcessor.processPaymentAndScheduleNextPayment(billPayment);
            }
        }
        return false;
    }

    public boolean validateProcessedPayments(final List<ProcessedBillPayment> processedBillPayments)
    {
        for(ProcessedBillPayment processedBillPayment : processedBillPayments)
        {
            return billPaymentProcessor.validateProcessedPayment(processedBillPayment);
        }
        return false;
    }

    public TreeMap<LocalDate, List<BillPayment>> groupBillPaymentsByPaymentDate(Collection<BillPayment> billPayments)
    {
        TreeMap<LocalDate, List<BillPayment>> billPaymentsByDate = new TreeMap<>();
        if(billPayments == null || billPayments.isEmpty())
        {
            return new TreeMap<>();
        }
        return null;
    }


    @Override
    public void run() {
        // Fetch all the BillPayments
        Collection<BillPaymentEntity> billPaymentEntities = getAllBillPayments();
        // Fetch all the BillPaymentSchedules
        Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities = getAllBillPaymentSchedules();

        // Next Convert the Entities to a BillPayment Model
        Collection<BillPayment> billPayments = getBillPaymentEntitiesConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);

        // Next group the Bill Payments by payment date
        TreeMap<LocalDate, List<BillPayment>> groupedPaymentsByPaymentDate = groupBillPaymentsByPaymentDate(billPayments);

        // BillPaymentRunner will execute



    }
}
