package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BillPaymentEngine
{
    void autoPayBills(List<BillPayment> billPayments);

    boolean paymentVerification(BillPayment billPayment);

    LocalDate getNextPaymentDate(BillPaymentSchedule billPaymentSchedule);

    void updateNextPaymentDate(BillPaymentHistory billPaymentHistory);

    void updateLastPaymentDate(BillPaymentHistory billPaymentHistory);

    LocalDate getLastPaymentDate(BillPaymentSchedule billPaymentSchedule);

    ConfirmationNumber generateConfirmationNumberForBill(BillPayment billPayment);

    void processRegularBillStatements(List<BillPayment> billPayments);

    ScheduleStatus updatePaymentStatus(BillPaymentSchedule billPayment);

    void processMissedPayments(List<BillPayment> billPayments);

    void processLatePayments(List<BillPayment> billPayments);

    void processLateFeesForLatePayments(List<BillPayment> latePayments);

    boolean sendLatePaymentNotification();

    void updateBalanceHistory(BalanceHistory balanceHistory);

    void updateAccountDetails(AccountDetails accountDetails);

    void updateTransactionDetails(TransactionDetail transactionDetail);

    boolean sendNotificationsToAccount();

    List<BillPayment> getBillPaymentsFromService();

    List<BillPaymentSchedule> getBillPaymentSchedulesFromService();

    List<BillPaymentHistory> getBillPaymentHistoriesFromService();


}
