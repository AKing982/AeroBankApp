package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.entity.LatePaymentEntity;
import com.example.aerobankapp.exceptions.InvalidBalanceException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.workbench.data.AccountDataManager;
import com.example.aerobankapp.workbench.data.BalanceHistoryDataManager;
import com.example.aerobankapp.workbench.data.BillPaymentHistoryDataManager;
import com.example.aerobankapp.workbench.data.LatePaymentDataManager;
import com.example.aerobankapp.workbench.generator.confirmation.ConfirmationNumberGenerator;
import com.example.aerobankapp.workbench.scheduler.BillPaymentScheduler;
import com.example.aerobankapp.workbench.utilities.AccountNotificationUtil;
import com.example.aerobankapp.workbench.utilities.notifications.ProcessedBillPaymentNotificationSender;
import com.example.aerobankapp.workbench.verification.PaymentVerifier;
import com.example.aerobankapp.workbench.verification.ProcessedBillPaymentVerification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Component
public class BillPaymentProcessor extends PaymentProcessor<BillPayment, ProcessedBillPayment> implements ScheduledPaymentProcessor<BillPayment>
{
    private final AccountDataManager accountDataManager;
    private final BalanceHistoryDataManager balanceHistoryDataManager;
    private final BillPaymentHistoryDataManager billPaymentHistoryDataManager;
    private final PaymentVerifier<ProcessedBillPayment> processedPaymentVerifier;
    private final BillPaymentScheduler billPaymentScheduler;
    private final ProcessedBillPaymentNotificationSender processedBillPaymentNotificationSender;
    private final LatePaymentDataManager latePaymentDataManager;
    private TreeMap<LocalDate, List<ProcessedBillPayment>> processedBillPayments = new TreeMap<>();
    private TreeMap<LocalDate, List<ProcessedBillPayment>> failedBillPayments = new TreeMap<>();
    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentProcessor.class);

    public BillPaymentProcessor(ConfirmationNumberGenerator confirmationNumberGenerator, AccountDataManager accountDataManager, BalanceHistoryDataManager balanceHistoryDataManager, BillPaymentHistoryDataManager billPaymentHistoryDataManager, PaymentVerifier<ProcessedBillPayment> processedPaymentVerifier, BillPaymentScheduler billPaymentScheduler, ProcessedBillPaymentNotificationSender processedBillPaymentNotificationSender, LatePaymentDataManager latePaymentDataManager) {
        super(confirmationNumberGenerator);
        this.accountDataManager = accountDataManager;
        this.balanceHistoryDataManager = balanceHistoryDataManager;
        this.billPaymentHistoryDataManager = billPaymentHistoryDataManager;
        this.processedPaymentVerifier = processedPaymentVerifier;
        this.billPaymentScheduler = billPaymentScheduler;
        this.processedBillPaymentNotificationSender = processedBillPaymentNotificationSender;
        this.latePaymentDataManager = latePaymentDataManager;
    }


    public void addFailedPayment(final ProcessedBillPayment processedBillPayment) {
        this.failedBillPayments.computeIfAbsent(processedBillPayment.getBillPayment().getDueDate(), k -> new ArrayList<>()).add(processedBillPayment);
    }

    public void addFailedPayments(final List<ProcessedBillPayment> processedBillPayments) {
        for (ProcessedBillPayment processedBillPayment : processedBillPayments) {
            LocalDate dueDate = processedBillPayment.getBillPayment().getDueDate();
            this.failedBillPayments.computeIfAbsent(dueDate, k -> new ArrayList<>()).add(processedBillPayment);
        }
    }

    public void handleMissedPayments(List<BillPayment> billPayments) {

    }

    public TreeMap<String, LocalDate> createNextPaymentDetails(BillPayment billPayment) {
        return billPaymentScheduler.getNextPaymentDetails(billPayment);
    }

    @Override
    public TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment(BillPayment payment) {
        TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap = new TreeMap<>();
        if(payment == null){
            throw new InvalidBillPaymentException("Unable to process null bill payment.");
        }

        if(isBillPaymentLate(payment)){
            // Create the Late Bill Payment
            LateBillPayment lateBillPayment = latePaymentDataManager.buildLatePayment(payment);
            LatePaymentEntity latePayment = latePaymentDataManager.createPaymentEntity(lateBillPayment);
            // Persist the Late Bill Payment to the database
            latePaymentDataManager.saveLatePayment(latePayment);

        }
        processSinglePayment(payment);
        Optional<LocalDate> nextScheduledPaymentDate = getNextScheduledPaymentDate(payment);
        nextScheduledPaymentMap.put(nextScheduledPaymentDate.orElseThrow(() -> new IllegalStateException("Next scheduled payment date not found")), payment.getPaymentAmount());

        return nextScheduledPaymentMap;
    }

    public boolean isBillPaymentLate(BillPayment payment){
        return payment.getScheduledPaymentDate().isAfter(payment.getDueDate());
    }

    public ProcessedBillPayment processSinglePayment(BillPayment payment) {
        // Validate the payment
        validateBillPayment(payment);

        // Extract the Current Account Balance
        int acctID = payment.getAccountCode().getSequence();
        LOGGER.info("AcctID: {}", acctID);
        processNewPaymentBalance(acctID, payment.getPaymentAmount());

        // Schedule the next payment date
        Optional<LocalDate> nextScheduledPaymentDate = getNextScheduledPaymentDate(payment);
        if(nextScheduledPaymentDate.isPresent()){
            LOGGER.info("Next Scheduled Payment Date: {}", nextScheduledPaymentDate);
            ProcessedBillPayment processedBillPayment = buildProcessedBillPayment(payment, nextScheduledPaymentDate.get());
            LOGGER.info("Processed Bill Payment: {}", processedBillPayment);
            // Create a BillPayment History object
            processBillPaymentHistory(processedBillPayment);
            validateProcessedPayment(processedBillPayment);
            sendNotification(processedBillPayment);
            return processedBillPayment;
        }else{
            LOGGER.error("Error: Next scheduled payment date not found for payment {}", payment);
            throw new IllegalStateException("Next Scheduled Payment date not found for payment " + payment.toString());
        }

    }

    public void processNewPaymentBalance(int acctID, BigDecimal paymentAmount){
        BigDecimal currentBalance = retrieveBalance(acctID);
        BigDecimal newBalance = getNewBalanceAfterPayment(paymentAmount, currentBalance);
        paymentPostProcessing(newBalance, acctID, currentBalance, paymentAmount);
    }

    public void sendNotification(final ProcessedBillPayment processedBillPayment) {
        // Send notification to the user that the payment has been processed
        LOGGER.info("Building Notification");
        StringBuilder paymentMessage = processedBillPaymentNotificationSender.createMessage(processedBillPayment);
        AccountNotification accountNotification = AccountNotificationUtil.buildAccountNotificationModel(paymentMessage, processedBillPayment);
        LOGGER.info("Sending Notification");
        processedBillPaymentNotificationSender.send(accountNotification);
    }

    public Optional<LocalDate> getNextScheduledPaymentDate(final BillPayment payment) {
        Optional<LocalDate> nextScheduledPaymentDate = billPaymentScheduler.getNextPaymentDate(payment);
        if(nextScheduledPaymentDate.isPresent()){
            return nextScheduledPaymentDate;
        }else{
            throw new IllegalStateException("Next scheduled payment date not found for payment " + payment.toString());
        }
    }

    public void processBillPaymentHistory(final ProcessedBillPayment processedBillPayment) {
        LOGGER.info("Processing Bill Payment History");
        BillPaymentHistory billPaymentHistory = billPaymentHistoryDataManager.createBillPaymentHistoryModel(processedBillPayment);
        // Save the BillPaymentHistory entity
        billPaymentHistoryDataManager.updateBillPaymentHistory(billPaymentHistory);
        LOGGER.info("Saved Bill Payment History");

        // Update the BillPayment History isProcessed to true
        billPaymentHistoryDataManager.updateIsProcessed(billPaymentHistory);
    }

    public boolean validateProcessedPayment(final ProcessedBillPayment processedBillPayment) {
        // Validate the payment has been processed
        LOGGER.info("Verifying the payment has been processed");
        if(processedPaymentVerifier.verify(processedBillPayment)){
            return true;
        }
        return false;
    }

    public ProcessedBillPayment buildProcessedBillPayment(BillPayment payment, LocalDate nextScheduledDate) {
        return new ProcessedBillPayment(payment, true, payment.getScheduledPaymentDate(), nextScheduledDate);
    }

    public void paymentPostProcessing(BigDecimal newBalance, int acctID, BigDecimal prevBalance, BigDecimal amount){

        updateAccountBalance(newBalance, acctID);

        // Create a balance history object
        BalanceHistory balanceHistory = createBalanceHistory(newBalance, acctID, prevBalance, amount);

        // update the balance history
        balanceHistoryDataManager.updateBalanceHistory(balanceHistory);
    }

    private BalanceHistory createBalanceHistory(BigDecimal newBalance, int acctID, BigDecimal prevBalance, BigDecimal adjusted){
        return balanceHistoryDataManager.createBalanceHistoryModel(newBalance, adjusted, prevBalance, acctID);
    }

    private BigDecimal getNewBalanceAfterPayment(BigDecimal paymentAmount, BigDecimal currentBalance){
        BigDecimal newBalance = currentBalance.subtract(paymentAmount);
        if(newBalance == null){
            throw new InvalidBalanceException("New balance after payment is null");
        }
        return newBalance;
    }

    private void updateAccountBalance(BigDecimal newBalance, int acctID){
        accountDataManager.updateAccountBalance(newBalance, acctID);
    }

    private BigDecimal retrieveBalance(final int acctID){
        BigDecimal currentBalance = accountDataManager.getCurrentAccountBalance(acctID);
        if(currentBalance == null){
            throw new InvalidBalanceException("Couldn't find current account balance");
        }
        return currentBalance;
    }

    @Override
    public List<ProcessedBillPayment> processPayments(List<BillPayment> payments) {
        return List.of();
    }

    private void validateBillPayment(final BillPayment billPayment) {
        if(billPayment == null){
            throw new InvalidBillPaymentException("BillPayment is null");
        }
        validateBillPaymentCriteria(billPayment);
    }

    private void validateBillPaymentCriteria(BillPayment billPayment) {
        if (billPayment.getPaymentAmount() == null || billPayment.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBillPaymentException("Payment amount is invalid");
        }
        if (billPayment.getDueDate() == null) {
            throw new InvalidBillPaymentException("Due date is null");
        }
        if (billPayment.getScheduledPaymentDate() == null) {
            throw new InvalidBillPaymentException("Scheduled payment date is null");
        }
        if (billPayment.getScheduleStatus() == null) {
            throw new InvalidBillPaymentException("Schedule status is null");
        }
        if (billPayment.getScheduleFrequency() == null) {
            throw new InvalidBillPaymentException("Schedule frequency is null");
        }
    }


    /**
     * Processes the auto payments for a list of bill payments.
     *
     * @param payments the list of bill payments to process
     * @return the list of processed bill payments
     */
    public List<ProcessedBillPayment> processAutoPayments(List<BillPayment> payments) {
        return null;
    }

}
