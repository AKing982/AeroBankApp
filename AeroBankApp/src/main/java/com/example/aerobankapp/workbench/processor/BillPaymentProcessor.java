package com.example.aerobankapp.workbench.processor;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.exceptions.InvalidBalanceException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.workbench.AccountNotificationCategory;
import com.example.aerobankapp.workbench.data.AccountDataManager;
import com.example.aerobankapp.workbench.data.BalanceHistoryDataManager;
import com.example.aerobankapp.workbench.data.BillPaymentHistoryDataManager;
import com.example.aerobankapp.workbench.scheduler.BillPaymentScheduler;
import com.example.aerobankapp.workbench.utilities.notifications.BillPaymentNotificationSender;
import com.example.aerobankapp.workbench.utilities.notifications.SystemNotificationSender;
import com.example.aerobankapp.workbench.verification.PaymentVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Component
public class BillPaymentProcessor implements PaymentProcessor<BillPayment, ProcessedBillPayment>, ScheduledPaymentProcessor<BillPayment>
{
    private final AccountDataManager accountDataManager;
    private final BalanceHistoryDataManager balanceHistoryDataManager;
    private final BillPaymentHistoryDataManager billPaymentHistoryDataManager;
    private final PaymentVerifier<ProcessedBillPayment> processedPaymentVerifier;
    private final BillPaymentScheduler billPaymentScheduler;
    private final BillPaymentNotificationSender billPaymentNotificationSender;
    private TreeMap<LocalDate, List<ProcessedBillPayment>> processedBillPayments = new TreeMap<>();
    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentProcessor.class);

    @Autowired
    public BillPaymentProcessor(AccountDataManager accountDataManager,
                                BalanceHistoryDataManager balanceHistoryDataManager,
                                BillPaymentHistoryDataManager billPaymentHistoryDataManager,
                                PaymentVerifier<ProcessedBillPayment> processedPaymentVerifier,
                                BillPaymentScheduler billPaymentScheduler,
                                BillPaymentNotificationSender billPaymentNotificationSender){
        this.accountDataManager = accountDataManager;
        this.balanceHistoryDataManager = balanceHistoryDataManager;
        this.billPaymentHistoryDataManager = billPaymentHistoryDataManager;
        this.processedPaymentVerifier = processedPaymentVerifier;
        this.billPaymentScheduler = billPaymentScheduler;
        this.billPaymentNotificationSender = billPaymentNotificationSender;
    }

    @Override
    public TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment(BillPayment payment) {
        TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap = new TreeMap<>();
        if(payment == null){
            throw new InvalidBillPaymentException("Unable to process null bill payment.");
        }

//        if(!validatePaymentDatePriorDueDate(billPayment)){
//            LateBillPayment lateBillPayment = buildLatePayment(billPayment);
//            processLatePayment(lateBillPayment, nextScheduledPaymentMap);
//        }else{
//            processOnTimePayment(billPayment, nextScheduledPaymentMap);
//        }

        return nextScheduledPaymentMap;
    }

    public ProcessedBillPayment processSinglePayment(BillPayment payment) {
        // Validate the payment
        validateBillPayment(payment);

        // Extract the Current Account Balance
        int acctID = payment.getAccountCode().getSequence();
        BigDecimal newBalance = calculateNewPaymentBalance(acctID, payment.getPaymentAmount());

        // Schedule the next payment date
        Optional<LocalDate> nextScheduledPaymentDate = getNextScheduledPaymentDate(payment);

        ProcessedBillPayment processedBillPayment = buildProcessedBillPayment(payment, nextScheduledPaymentDate.get());
        // Create a BillPayment History object
        processBillPaymentHistory(processedBillPayment);
        validateProcessedPayment(processedBillPayment);
        sendNotification(processedBillPayment);
        return processedBillPayment;
    }

    public BigDecimal calculateNewPaymentBalance(int acctID, BigDecimal paymentAmount){
        BigDecimal currentBalance = retrieveBalance(acctID);
        BigDecimal newBalance = getNewBalanceAfterPayment(paymentAmount, currentBalance);
        paymentPostProcessing(newBalance, acctID, currentBalance, paymentAmount);
        return newBalance;
    }

    public void sendNotification(ProcessedBillPayment processedBillPayment) {
        // Send notification to the user that the payment has been processed
        AccountNotification accountNotification = createAccountNotificationFromPayment(processedBillPayment);

        billPaymentNotificationSender.send(accountNotification);
    }

    public Optional<LocalDate> getNextScheduledPaymentDate(BillPayment payment) {
        return billPaymentScheduler.getNextPaymentDate(payment);
    }

    public void processBillPaymentHistory(ProcessedBillPayment processedBillPayment) {
        BillPaymentHistory billPaymentHistory = billPaymentHistoryDataManager.createBillPaymentHistoryModel(processedBillPayment);
        // Save the BillPaymentHistory entity
        billPaymentHistoryDataManager.updateBillPaymentHistory(billPaymentHistory);

        // Update the BillPayment History isProcessed to true
        billPaymentHistoryDataManager.updateIsProcessed(billPaymentHistory);
    }

    public void validateProcessedPayment(ProcessedBillPayment processedBillPayment) {
        // Validate the payment has been processed
        processedPaymentVerifier.verify(processedBillPayment);
    }

    public AccountNotification createAccountNotificationFromPayment(ProcessedBillPayment processedBillPayment) {
        AccountNotification accountNotification = new AccountNotification();
        accountNotification.setSevere(false);
        accountNotification.setAccountID(processedBillPayment.getBillPayment().getAccountCode().getSequence());
        accountNotification.setRead(false);
        accountNotification.setTitle();
        accountNotification.setPriority(1);
        accountNotification.setMessage();
        return accountNotification;
    }

    private ProcessedBillPayment buildProcessedBillPayment(BillPayment payment, LocalDate nextScheduledDate) {
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

    public void saveBillPaymentHistoryToDatabase(BillPaymentHistory billPaymentHistory){
        billPaymentHistoryDataManager.updateBillPaymentHistory(billPaymentHistory);
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

    private BigDecimal retrieveBalance(int acctID){
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

    private void validateBillPayment(BillPayment billPayment) {
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
