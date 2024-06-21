package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.converter.BillPaymentConverter;
import com.example.aerobankapp.engine.BillPaymentEngine;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.BillPaymentIdCriteria;
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
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Data
public class BillPaymentRunner implements Runnable
{
    private final BillPaymentProcessor billPaymentProcessor;
    private final BillPaymentConverter billPaymentConverter;
    private BillPaymentDataManager billPaymentDataManager;
    private BillPaymentIdCriteria billPaymentIdCriteria;
    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentRunner.class);
    private TreeMap<LocalDate, Collection<BillPayment>> groupedBillPaymentsByDate = new TreeMap<>();

    @Autowired
    public BillPaymentRunner(BillPaymentProcessor billPaymentProcessor,
                             BillPaymentConverter billPaymentConverter,
                             BillPaymentDataManager billPaymentDataManager,
                             BillPaymentIdCriteria billPaymentIdCriteria) {
        this.billPaymentProcessor = billPaymentProcessor;
        this.billPaymentConverter = billPaymentConverter;
        this.billPaymentDataManager = billPaymentDataManager;
        this.billPaymentIdCriteria = billPaymentIdCriteria;
    }


    public Optional<BillPaymentEntity> getBillPaymentById(Long paymentId)
    {
        return billPaymentDataManager.findBillPaymentByID(paymentId);
    }

    public Optional<BillPaymentScheduleEntity> getBillPaymentScheduleById(Long paymentScheduleId)
    {
        return billPaymentDataManager.findBillPaymentScheduleByID(paymentScheduleId);
    }

    public Collection<BillPayment> getBillPaymentEntitiesConvertedToBillPaymentModel(final Optional<BillPaymentEntity> billPaymentEntity, final Optional<BillPaymentScheduleEntity> billPaymentScheduleEntity) {
        Collection<BillPayment> billPayments = new ArrayList<>();
        if(billPaymentEntity.isPresent()) {
            if(billPaymentScheduleEntity.isPresent()) {
                BillPaymentEntity billPaymentEntity1 = billPaymentEntity.get();
                BillPaymentScheduleEntity billPaymentScheduleEntity1 = billPaymentScheduleEntity.get();
                BillPayment billPayment = billPaymentConverter.convert(billPaymentEntity1, billPaymentScheduleEntity1);
                billPayments.add(billPayment);
            }
        }
        return billPayments;
    }

    public boolean scheduleAndExecutePayments(final Collection<BillPayment> billPayments)
    {
       return false;
    }


    @Override
    public void run() {

        Long paymentId = billPaymentIdCriteria.getPaymentId();
        Long scheduleId = billPaymentIdCriteria.getScheduleId();

        // Fetch all the BillPayments
        Optional<BillPaymentEntity> billPaymentEntities = getBillPaymentById(paymentId);
        // Fetch all the BillPaymentSchedules
        Optional<BillPaymentScheduleEntity> billPaymentScheduleEntities = getBillPaymentScheduleById(scheduleId);

        // Next Convert the Entities to a BillPayment Model
        Collection<BillPayment> billPayments = getBillPaymentEntitiesConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);

        scheduleAndExecutePayments(billPayments);
    }
}
