package com.example.aerobankapp.workbench.runner;

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
import com.example.aerobankapp.workbench.queues.BillPaymentQueue;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * This class will manage when the BillPaymentRunner will run and execute tasks
 */
@Component
public class BillPaymentRunner implements Runnable
{
    private final BillPaymentEngine billPaymentEngine;
    private final BillPaymentDataManager billPaymentDataManager;
    private RabbitTemplate rabbitTemplate;
    private final BillPaymentQueue billPaymentQueue;

    @Autowired
    public BillPaymentRunner(@Qualifier("billPaymentEngineImpl") BillPaymentEngine billPaymentEngine,
                             @Qualifier("billPaymentDataManagerImpl") BillPaymentDataManager billPaymentDataManager,
                             BillPaymentQueue billPaymentQueue) {
        this.billPaymentEngine = billPaymentEngine;
        this.billPaymentDataManager = billPaymentDataManager;
        this.billPaymentQueue = billPaymentQueue;
    }

    public Queue<BillPayment> getCurrentQueueState(){
        return billPaymentQueue.getQueueState();
    }

    public void processPaymentsForUser(int userID)
    {
        
    }

    public void resetQueueState(){
        billPaymentQueue.clear();
    }

    public boolean isBillPaymentQueueEmpty(){
        return billPaymentQueue.isEmpty();
    }

    public void updateNextScheduledRunDatesToQueue(final List<BillPaymentSchedule> billPaymentSchedules){

    }

    public List<BillPaymentEntity> loadBillPayments(){
        return null;
    }

    public void processBillPaymentsFromQueue(){

    }

    public void archiveProcessedPayments(final List<BillPayment> processedBillPayments){

    }

    public boolean scheduleNextBatch(final Map<ScheduleFrequency, List<BillPayment>> groupedPayments){
        return false;
    }

    public LocalDate buildNextScheduledDate(final LocalDate currentDate, final ScheduleFrequency scheduleFrequency){
        return null;
    }

    public TreeMap<LocalDate, List<BillPayment>> groupBillPaymentsByDateAndFrequency(final List<BillPayment> billPayments){
        return null;
    }


    @Override
    public void run() {

    }
}
