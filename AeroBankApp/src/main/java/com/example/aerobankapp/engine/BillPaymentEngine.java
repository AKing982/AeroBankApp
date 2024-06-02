package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.*;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

public interface BillPaymentEngine
{
    TreeMap<LocalDate, ProcessedBillPayment> autoPayBills(TreeMap<LocalDate, AutoPayBillPayment> billPayments);

    boolean paymentVerification(ProcessedBillPayment processedBillPayment);

    LocalDate getNextPaymentDate(BillPayment billPaymentSchedule);

    void updateNextPaymentDate(BillPaymentHistory billPaymentHistory);

    void updateLastPaymentDate(BillPaymentHistory billPaymentHistory);

    LocalDate getLastPaymentDate(BillPayment billPaymentSchedule);

    ConfirmationNumber generateConfirmationNumberForBill(BillPayment billPayment);

    void processRegularBillStatements(TreeMap<LocalDate, BillPayment> billPayments);

    ScheduleStatus updatePaymentStatus(BillPayment billPayment);


    void processLatePayments(TreeMap<LocalDate, LateBillPayment> billPayments);

    void processLateFeesForLatePayments(TreeMap<LocalDate, BillPayment> latePayments);

    boolean sendLatePaymentNotification();

    TreeMap<LocalDate, ProcessedBillPayment> processPayments(TreeMap<LocalDate, ? extends BillPayment> payments);

    ProcessedBillPayment processSinglePayment(BillPayment billPayment);

    void updateBalanceHistory(BalanceHistory balanceHistory);

    void updateAccountDetails(AccountDetails accountDetails);

    void updateTransactionDetails(TransactionDetail transactionDetail);

    boolean sendNotificationsToAccount();

}
