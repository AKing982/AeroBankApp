package com.example.aerobankapp.engine;

import com.example.aerobankapp.exceptions.InvalidBillPaymentParametersException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.BillPaymentNotificationService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillPaymentEngineImpl implements BillPaymentEngine
{
    private final RabbitTemplate rabbitTemplate;
    private final BillPaymentScheduleService billPaymentScheduleService;
    private final BillPaymentService billPaymentService;
    private final BillPaymentNotificationService billPaymentNotificationService;

    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentEngineImpl.class);

    @Autowired
    public BillPaymentEngineImpl(RabbitTemplate rabbitTemplate,
                                 BillPaymentScheduleService billPaymentScheduleService,
                                 BillPaymentService billPaymentService,
                                 BillPaymentNotificationService billPaymentNotificationService){
        this.rabbitTemplate = rabbitTemplate;
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentService = billPaymentService;
        this.billPaymentNotificationService = billPaymentNotificationService;
    }


    @Override
    public List<ProcessedBillPayment> autoPayBills(final List<AutoPayBillPayment> billPayments) {
        if(billPayments.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to process Auto-Payed bills due to empty list.");
        }

        return processPayments(billPayments);
    }


    @Override
    public boolean paymentVerification(BillPayment billPayment) {
        return false;
    }

    @Override
    public LocalDate getNextPaymentDate(BillPayment billPaymentSchedule) {
        return null;
    }

    @Override
    public void updateNextPaymentDate(BillPaymentHistory billPaymentHistory) {

    }

    @Override
    public void updateLastPaymentDate(BillPaymentHistory billPaymentHistory) {

    }

    @Override
    public LocalDate getLastPaymentDate(BillPayment billPayment) {
        return null;
    }

    @Override
    public ConfirmationNumber generateConfirmationNumberForBill(BillPayment billPayment) {
        return null;
    }

    @Override
    public void processRegularBillStatements(List<BillPayment> billPayments) {

    }

    @Override
    public ScheduleStatus updatePaymentStatus(BillPayment billPayment) {
        return null;
    }

    @Override
    public void processLatePayments(List<LateBillPayment> billPayments) {

    }

    @Override
    public void processLateFeesForLatePayments(List<BillPayment> latePayments) {

    }

    @Override
    public boolean sendLatePaymentNotification() {
        return false;
    }

    @Override
    public List<ProcessedBillPayment> processPayments(List<? extends BillPayment> payments) {
        if(payments.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to process payments from empty list.");
        }
        if(!assertBillPaymentParametersNotNull(payments)){
            throw new InvalidBillPaymentParametersException("Unable to process payments due to null bill payments.");
        }

        return null;
    }

    private boolean assertBillPaymentParametersNotNull(List<? extends BillPayment> payments){
        for(BillPayment payment : payments){
            if(payment.getPaymentAmount() == null ||
                payment.getPaymentType() == null ||
                payment.getScheduledPaymentDate() == null ||
                payment.getAccountCode() == null ||
            payment.getDueDate() == null ||
            payment.getScheduleFrequency() == null ||
            payment.getScheduleStatus() == null){
                LOGGER.error("Found Null Bill Parameters: " + payment.toString());
                return false;
            }
        }
        return true;
    }


    @Override
    public void updateBalanceHistory(BalanceHistory balanceHistory) {

    }

    @Override
    public void updateAccountDetails(AccountDetails accountDetails) {

    }

    @Override
    public void updateTransactionDetails(TransactionDetail transactionDetail) {

    }

    @Override
    public boolean sendNotificationsToAccount() {
        return false;
    }

    @Override
    public List<BillPayment> getBillPaymentsFromService() {
        return null;
    }


    @Override
    public List<BillPaymentHistory> getBillPaymentHistoriesFromService() {
        return null;
    }
}
