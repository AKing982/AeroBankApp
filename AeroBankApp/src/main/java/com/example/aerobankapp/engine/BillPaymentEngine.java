package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.*;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

public interface BillPaymentEngine
{
    List<ProcessedBillPayment> autoPayBills(List<BillPayment> billPayments);

    boolean paymentVerification(ProcessedBillPayment processedBillPayment);

    LocalDate calculateNextPaymentDate(LocalDate currentDate, ScheduleFrequency frequency);

    void updateNextPaymentDate(BillPaymentHistory billPaymentHistory);

    void updateLastPaymentDate(BillPaymentHistory billPaymentHistory);

    LocalDate getLastPaymentDate(BillPayment billPaymentSchedule);

    void processOnTimePayment(BillPayment billPayment, TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap);

    ConfirmationNumber generateConfirmationNumberForBill(BillPayment billPayment);

    void processRegularBillStatements(TreeMap<LocalDate, BillPayment> billPayments);

    void updatePaymentStatus(BillPayment billPayment);

    LateBillPayment buildLatePayment(BillPayment payment);

    void processLatePayment(LateBillPayment lateBillPayment, TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap);

    boolean sendLatePaymentNotification();

    TreeMap<LocalDate, ProcessedBillPayment> processPayments(TreeMap<LocalDate, ? extends BillPayment> payments);

    ProcessedBillPayment processSinglePayment(BillPayment billPayment);

    void updateBalanceHistory(BalanceHistory balanceHistory);

    void updateAccountDetails(AccountDetails accountDetails);

    void updateTransactionDetails(TransactionDetail transactionDetail);

    boolean sendNotificationsToAccount();

    void handleDailyPayments(TreeMap<BillPayment, BillPaymentSchedule> billPayments);

    void handleWeeklyPayments(TreeMap<BillPayment, BillPaymentSchedule> weeklyPayments);

    void handleMonthlyPayments(TreeMap<BillPayment, BillPaymentSchedule> monthlyPayments);

    void handleBiWeeklyPayments(TreeMap<BillPayment, BillPaymentSchedule> biWeeklyPayments);



}
