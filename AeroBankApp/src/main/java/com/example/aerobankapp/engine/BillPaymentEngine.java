package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.*;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;

import java.time.LocalDate;
import java.util.List;

public interface BillPaymentEngine
{
    List<ProcessedBillPayment> autoPayBills(List<AutoPayBillPayment> billPayments);

    boolean paymentVerification(ProcessedBillPayment processedBillPayment);

    LocalDate getNextPaymentDate(BillPayment billPaymentSchedule);

    void updateNextPaymentDate(BillPaymentHistory billPaymentHistory);

    void updateLastPaymentDate(BillPaymentHistory billPaymentHistory);

    LocalDate getLastPaymentDate(BillPayment billPaymentSchedule);

    ConfirmationNumber generateConfirmationNumberForBill(BillPayment billPayment);

    void processRegularBillStatements(List<BillPayment> billPayments);

    ScheduleStatus updatePaymentStatus(BillPayment billPayment);


    void processLatePayments(List<LateBillPayment> billPayments);

    void processLateFeesForLatePayments(List<BillPayment> latePayments);

    boolean sendLatePaymentNotification();

    List<ProcessedBillPayment> processPayments(List<? extends BillPayment> payments);

    ProcessedBillPayment processSinglePayment(BillPayment billPayment);

    void updateBalanceHistory(BalanceHistory balanceHistory);

    void updateAccountDetails(AccountDetails accountDetails);

    void updateTransactionDetails(TransactionDetail transactionDetail);

    boolean sendNotificationsToAccount();

}
