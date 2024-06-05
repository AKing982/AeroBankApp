package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.BillPaymentHistory;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

@Component
public class BillPaymentProcessor implements PaymentProcessor<BillPayment>
{
    private LatePaymentProcessor latePaymentProcessor;
    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentProcessor.class);

    @Autowired
    public BillPaymentProcessor(LatePaymentProcessor latePaymentProcessor){
        this.latePaymentProcessor = latePaymentProcessor;
    }

    @Override
    public TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment(BillPayment payment) {
        TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap = new TreeMap<>();
        if(payment == null){
            throw new InvalidBillPaymentException("Unable to process null bill payment.");
        }

        if(!validatePaymentDatePriorDueDate(payment)){
            LateBillPayment lateBillPayment = latePaymentProcessor.buildLateBillPayment(payment);
            latePaymentProcessor.processLatePayment(lateBillPayment, nextScheduledPaymentMap);
        }else{
            processOnTimePayment(payment, nextScheduledPaymentMap);
        }
        return nextScheduledPaymentMap;
    }

    public ProcessedBillPayment processSinglePayment(BillPayment payment) {
        BigDecimal paymentAmount = billPayment.getPaymentAmount();
        validatePaymentAmount(paymentAmount);

        LocalDate paymentDate = billPayment.getScheduledPaymentDate();
        validatePaymentDate(paymentDate);

        TreeMap<LocalDate, BigDecimal> nextScheduledPayment = processPaymentAndScheduleNextPayment(billPayment);
        // Get the current Account Balance
        LocalDate nextPaymentDate = nextScheduledPayment.firstKey();

        ProcessedBillPayment processedBillPayment = buildProcessedBillPayment(nextPaymentDate, billPayment);
        LOGGER.info("Processed Bill Payment: {}", processedBillPayment);

        BillPaymentHistory billPaymentHistory = createBillPaymentHistoryModel(processedBillPayment);

        updateBillPaymentHistory(billPaymentHistory);

        // Validate the Processed Bill Payment
        validateProcessedBillPayment(processedBillPayment, billPaymentHistory);

        return processedBillPayment;
    }
}
