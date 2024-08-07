package com.example.aerobankapp.services;

import com.example.aerobankapp.model.AutoPayBillPayment;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.ProcessedBillPayment;
import com.example.aerobankapp.workbench.data.AccountDataManager;
import com.example.aerobankapp.workbench.data.BalanceHistoryDataManager;
import com.example.aerobankapp.workbench.data.BillPaymentHistoryDataManager;
import com.example.aerobankapp.workbench.utilities.notifications.ProcessedBillPaymentNotificationSender;
import com.example.aerobankapp.workbench.verification.PaymentVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

@Service
public class PaymentService
{
    private final AccountDataManager accountDataManager;
    private final BalanceHistoryDataManager balanceHistoryDataManager;
    private final BillPaymentHistoryDataManager billPaymentHistoryDataManager;
    private final PaymentVerifier<ProcessedBillPayment> processedBillPaymentPaymentVerifier;
    private final ProcessedBillPaymentNotificationSender processedBillPaymentNotificationSender;
    private final TreeMap<LocalDate, Collection<ProcessedBillPayment>> processedBillPayments = new TreeMap<>();

    @Autowired
    public PaymentService(AccountDataManager accountDataManager,
                          BalanceHistoryDataManager balanceHistoryDataManager,
                          BillPaymentHistoryDataManager billPaymentHistoryDataManager,
                          PaymentVerifier<ProcessedBillPayment> processedBillPaymentPaymentVerifier,
                          ProcessedBillPaymentNotificationSender processedBillPaymentNotificationSender) {
        this.accountDataManager = accountDataManager;
        this.balanceHistoryDataManager = balanceHistoryDataManager;
        this.billPaymentHistoryDataManager = billPaymentHistoryDataManager;
        this.processedBillPaymentPaymentVerifier = processedBillPaymentPaymentVerifier;
        this.processedBillPaymentNotificationSender = processedBillPaymentNotificationSender;
    }

    public void processSinglePayment(BillPayment billPayment)
    {

    }

    public Boolean verifyProcessedBillPayment(ProcessedBillPayment processedBillPayment)
    {
        return null;
    }

    private BigDecimal retrieveBalance(int acctID)
    {
        return null;
    }

    public void processAutoPayBills(Collection<AutoPayBillPayment> autoPayBillPayments)
    {

    }

    public void processNewAccountBalance(int acctID, BigDecimal paymentAmount)
    {

    }

    private void updateAccountBalance(BigDecimal newBalance, int acctID)
    {

    }

    public void processBillPaymentHistory(final ProcessedBillPayment processedBillPayment)
    {

    }

    public void paymentPostProcessing(BigDecimal newBalance, int acctID, BigDecimal prevBalance, BigDecimal amount)
    {

    }

    public void sendPaymentNotification(ProcessedBillPayment processedBillPayment)
    {

    }

    private void addProcessedPaymentToTreeMap(ProcessedBillPayment processedBillPayment)
    {
        this.processedBillPayments.put(processedBillPayment.getLastProcessedDate(), List.of(processedBillPayment));
    }
}
