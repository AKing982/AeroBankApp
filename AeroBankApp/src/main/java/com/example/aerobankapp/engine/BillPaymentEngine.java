package com.example.aerobankapp.engine;

import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.PaymentService;
import com.example.aerobankapp.workbench.data.LatePaymentDataManager;
import com.example.aerobankapp.workbench.processor.FailedBillPaymentProcessor;
import com.example.aerobankapp.workbench.scheduler.BillPaymentScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class BillPaymentEngine
{
    private BillPaymentScheduler billPaymentScheduler;
    private PaymentService paymentService;
    private LatePaymentDataManager latePaymentDataManager;
    private FailedBillPaymentProcessor failedBillPaymentProcessor;
    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentEngine.class);

    @Autowired
    public BillPaymentEngine(BillPaymentScheduler billPaymentScheduler, PaymentService paymentService, LatePaymentDataManager latePaymentDataManager)
    {
        this.billPaymentScheduler = billPaymentScheduler;
        this.paymentService = paymentService;
        this.latePaymentDataManager = latePaymentDataManager;
    }

    public void processPayment(BillPayment billPayment)
    {

    }

    public void processLatePayment(LateBillPayment lateBillPayment)
    {

    }


}
