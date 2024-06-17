package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.converter.BillPaymentConverter;
import com.example.aerobankapp.engine.BillPaymentEngine;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.BillPaymentSchedule;
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


    public Collection<BillPaymentEntity> getAllBillPayments() {
        Collection<BillPaymentEntity> billPaymentEntities = billPaymentDataManager.findAllBillPayments();
        if(billPaymentEntities == null) {
            return Collections.emptyList();
        }
        return billPaymentEntities;
    }

    public Collection<BillPaymentScheduleEntity> getAllBillPaymentSchedules() {
        return null;
    }

    public Collection<BillPayment> getBillPaymentEntitiesConvertedToBillPaymentModel(Collection<BillPaymentEntity> billPaymentEntities, Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities) {
        return null;
    }

    public boolean processPaymentForDate(LocalDate date)
    {
        return false;
    }

    public boolean scheduleAndExecuteAllPayments(TreeMap<LocalDate, Collection<BillPayment>> billPaymentsByDate)
    {
        return false;
    }

    public TreeMap<LocalDate, List<BillPayment>> groupBillPaymentsByPaymentDate(Collection<BillPayment> billPayments)
    {
        return null;
    }


    @Override
    public void run() {

    }
}
