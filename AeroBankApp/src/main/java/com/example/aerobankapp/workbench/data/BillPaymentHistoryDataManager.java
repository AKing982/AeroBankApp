package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.model.BillPaymentHistory;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BillPaymentHistoryDataManager
{
    private final BillPaymentHistoryService billPaymentHistoryService;

    @Autowired
    public BillPaymentHistoryDataManager(BillPaymentHistoryService billPaymentHistoryService){
        this.billPaymentHistoryService = billPaymentHistoryService;
    }

    public BillPaymentHistory createBillPaymentHistoryModel(ProcessedBillPayment processedBillPayment){
        return new BillPaymentHistory(processedBillPayment.getNextPaymentDate(), processedBillPayment.getLastProcessedDate(), LocalDate.now(), true);
    }
}
