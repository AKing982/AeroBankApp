package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.engine.BillPaymentEngine;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


/**
 * This class will manage when the BillPaymentRunner will run and schedule payment tasks
 */
@Component
public class BillPaymentRunner implements Runnable
{
    private final BillPaymentEngine billPaymentEngine;
    private final BillPaymentService billPaymentService;

    @Autowired
    public BillPaymentRunner(@Qualifier("billPaymentEngineImpl")BillPaymentEngine billPaymentEngine,
                             BillPaymentService billPaymentService){
        this.billPaymentEngine = billPaymentEngine;
        this.billPaymentService = billPaymentService;
    }

    public List<BillPaymentEntity> loadBillPayments(){
        return null;
    }

    public void executeAutoPayments(List<BillPaymentEntity> billPaymentEntities){

    }

    public LocalDate scheduleNextPaymentDate(){
        return null;
    }


    public void executeRegularBillPayments(){

    }

    @Override
    public void run() {

    }
}
