package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.engine.BillPaymentEngine;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillPaymentRunner implements Runnable
{
    private final BillPaymentEngine billPaymentEngine;
    private final BillPaymentService billPaymentService;
    private final BillPaymentScheduleService billPaymentScheduleService;
    private final BillPaymentHistoryService billPaymentHistoryService;

    @Autowired
    public BillPaymentRunner(@Qualifier("billPaymentEngineImpl")BillPaymentEngine billPaymentEngine,
                             BillPaymentService billPaymentService,
                             BillPaymentScheduleService billPaymentScheduleService,
                             BillPaymentHistoryService billPaymentHistoryService){
        this.billPaymentEngine = billPaymentEngine;
        this.billPaymentService = billPaymentService;
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentHistoryService = billPaymentHistoryService;
    }

    public List<BillPaymentEntity> loadBillPayments(){
        return null;
    }

    public void executeAutoPayments(List<BillPaymentEntity> billPaymentEntities){

    }

    @Override
    public void run() {

    }
}
