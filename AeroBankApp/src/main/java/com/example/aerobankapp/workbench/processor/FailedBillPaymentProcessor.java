package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.model.FailedBillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

@Service
public class FailedBillPaymentProcessor
{
    private TreeMap<LocalDate, List<FailedBillPayment>> failedBillPayments = new TreeMap<>();

    public FailedBillPaymentProcessor()
    {

    }

    public void addFailedPayment(final ProcessedBillPayment processedBillPayment) {

    }

    public void addFailedPayments(final List<ProcessedBillPayment> processedBillPayments) {

    }
}
