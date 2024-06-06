package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.services.builder.AccountNotificationEntityBuilderImpl;
import com.example.aerobankapp.services.builder.BillPaymentHistoryEntityBuilderImpl;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.billPayment.BillPaymentNotificationSender;
import com.example.aerobankapp.workbench.data.AccountDataManager;
import com.example.aerobankapp.workbench.data.BalanceHistoryDataManager;
import com.example.aerobankapp.workbench.data.BillPaymentDataManager;
import com.example.aerobankapp.workbench.data.BillPaymentHistoryDataManager;
import com.example.aerobankapp.workbench.generator.confirmation.ConfirmationNumberGenerator;
import com.example.aerobankapp.workbench.processor.LatePaymentProcessor;
import com.example.aerobankapp.workbench.processor.PaymentProcessor;
import com.example.aerobankapp.workbench.scheduler.PaymentScheduler;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import com.example.aerobankapp.workbench.verification.PaymentVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
public class BillPaymentEngineImpl implements BillPaymentEngine
{

    private final AccountDataManager accountDataManager;
    private final BalanceHistoryDataManager balanceHistoryDataManager;
    private final EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder;
    private final EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder;
    private EntityBuilder<BillPaymentHistoryEntity, BillPaymentHistory> billPaymentHistoryEntityBuilder;
    private PaymentVerifier<BillPayment> billPaymentVerifier;
    private PaymentVerifier<ProcessedBillPayment> processedBillPaymentVerifier;
    private PaymentProcessor<BillPayment> billPaymentProcessor;
    private PaymentProcessor<ProcessedBillPayment> processedBillPaymentProcessor;
    private PaymentProcessor<LatePaymentProcessor> latePaymentProcessorPaymentProcessor;
    private PaymentScheduler<BillPayment> billPaymentScheduler;
    private BillPaymentDataManager billPaymentDataManager;
    private BillPaymentNotificationSender billPaymentNotificationSender;
    private ConfirmationNumberGenerator confirmationNumberGenerator;
    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentEngineImpl.class);

    @Autowired
    public BillPaymentEngineImpl(AccountDataManager accountDataManager,
                                 BalanceHistoryDataManager balanceHistoryDataManager,
                                 BillPaymentNotificationSender billPaymentNotificationSender,
                                 ConfirmationNumberGenerator confirmationNumberGenerator,
                                 @Qualifier("accountDetailsEntityBuilderImpl") EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder,
                                 @Qualifier("balanceHistoryEntityBuilderImpl") EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder,
                                 EntityBuilder<BillPaymentHistoryEntity, BillPaymentHistory> billPaymentHistoryEntityBuilder){
        this.accountDataManager = accountDataManager;
        this.balanceHistoryDataManager = balanceHistoryDataManager;
        this.billPaymentNotificationSender = billPaymentNotificationSender;
        this.confirmationNumberGenerator = confirmationNumberGenerator;
        this.accountDetailsEntityBuilder = accountDetailsEntityBuilder;
        this.balanceHistoryEntityBuilder = balanceHistoryEntityBuilder;
        this.billPaymentHistoryEntityBuilder = new BillPaymentHistoryEntityBuilderImpl();
    }

    //TODO: To Implement 6/3/24
    /**
     * Automatically pays the given list of bill payments.
     *
     * @param billPayments The list of bill payments to auto-pay.
     * @return A list of processed bill payments.
     * @throws NonEmptyListRequiredException if the billPayments list is empty.
     * @throws InvalidBillPaymentException if a null payment is found in the list when there is only one payment.
     */
    @Override
    public List<ProcessedBillPayment> autoPayBills(final List<BillPayment> billPayments) {
        List<ProcessedBillPayment> processedBillPayments = new ArrayList<>();
        if(billPayments.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to process Auto-Payed bills due to empty list.");
        }

        // Does the Bill Payments have a single payment
        if(assertPaymentsSizeEqualToOne(billPayments)){
            // Is the payment null?
            BillPayment payment = billPayments.get(0);
            if(payment == null){
                throw new InvalidBillPaymentException("Caught Null Payment in list.");
            }
        }
        // Check if the single payment is null before proceeding to processing

        // Loop through the bill payments
        for(BillPayment billPayment : billPayments){
            if(billPayment == null){
                continue;
            }

            if(billPayment.isAutoPayEnabled()){
                    // Process the bill payment
                ProcessedBillPayment processedBillPayment = processSinglePayment(billPayment);

                // Send the notification to the user

                // Build the next scheduled payment date
            }
        }
        // Does the bill payment have auto pay enabled?
        // If auto pay is enabled, proceed to process the payment
        // Once the payment has been processed, send a notification to the user/account
        // build the next scheduled payment date
        return processedBillPayments;
    }

    private boolean assertPaymentsSizeEqualToOne(List<BillPayment> billPayments){
        return billPayments.size() == 1;
    }

    private boolean assertBillPaymentNull(BillPayment billPayment){
        return billPayment == null;
    }

    @Override
    public boolean paymentVerification(final ProcessedBillPayment processedBillPayment, final BillPaymentHistory billPaymentHistory) {
        if(processedBillPayment == null){
            throw new InvalidProcessedBillPaymentException("Processed bill payment cannot be null.");
        }

//        if(!paymentCriteriaVerified(processedBillPayment)){
//            return false;
//        }
        Long paymentID = getProcessedBillPaymentID(billPaymentHistory);
        try{
            validatePaymentWithDB(paymentID);
        } catch(Exception e) {
            LOGGER.error("Error while processing the payment", e);
        }
        return isPaymentProcessed(paymentID);
    }

    private void validatePaymentWithDB(Long paymentID){
        LOGGER.info("Validating Payment with ID: {}", paymentID);
        Optional<BillPaymentHistoryEntity> billPaymentEntityOptional = fetchBillPaymentFromDB(paymentID);
        LOGGER.info("Validating payment is Present in the database");
        if(paymentIsPresent(billPaymentEntityOptional)){
            // Is the isProcessed field set to true
            LOGGER.info("Payment has been processed");
        }
    }

    public LocalDate getNextPaymentDateFromPayment(BillPayment payment){

    }





    private boolean isPaymentProcessed(Long id){
        boolean isProcessed = billPaymentHistoryService.isPaymentProcessedById(id);
        LOGGER.info("Payment has been processed: {}", isProcessed);
        return isProcessed;
    }

    private Optional<BillPaymentHistoryEntity> fetchBillPaymentFromDB(final Long paymentID){
        Optional<BillPaymentHistoryEntity> optionalBillPaymentHistoryEntity = billPaymentHistoryService.findById(paymentID);
        if(optionalBillPaymentHistoryEntity.isPresent()){
            LOGGER.info("Fetching BillPaymentHistory: {}", optionalBillPaymentHistoryEntity.get());
        }else{
            LOGGER.warn("No BillPaymentHistory found for paymentID: {}", paymentID);
        }
        return optionalBillPaymentHistoryEntity;
    }

    private boolean paymentCriteriaVerified(final ProcessedBillPayment payment){
        return (payment.isComplete() && payment.getLastProcessedDate() != null);
    }

    private boolean paymentIsPresent(final Optional<BillPaymentHistoryEntity> billPaymentEntityOptional){
        return billPaymentEntityOptional.isPresent();
    }

    private Long getProcessedBillPaymentID(final BillPaymentHistory billPaymentHistory){
        return billPaymentHistory.getPaymentId();
    }


    private boolean isScheduledPaymentDateValid(BillPayment billPayment){
        return billPayment.getScheduledPaymentDate() != null;
    }

    private boolean isDueDateValid(BillPayment billPayment){
        return billPayment.getDueDate() != null;
    }

    @Override
    public void updateNextPaymentDate(BillPaymentHistory billPaymentHistory) {

    }

    @Override
    public void updateLastPaymentDate(BillPaymentHistory billPaymentHistory) {

    }

    @Override
    public LocalDate getLastPaymentDate(BillPayment billPayment) {

    }

    @Override
    public void processOnTimePayment(BillPayment billPayment, TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap) {
        // Retrieve the account code
        BigDecimal newBalance = calculateNewBalance(billPayment);
        updateAccountAndPostProcessing(billPayment, newBalance);
        scheduleNextPayment(billPayment, nextScheduledPaymentMap);
    }

    private BigDecimal calculateNewBalance(BillPayment billPayment){
        BigDecimal paymentAmount = getPaymentAmountCriteria(billPayment);
        AccountCode accountCode = getAccountCodeByPaymentCriteria(billPayment);
        int acctID = getAccountIDSegment(accountCode);
        BigDecimal currentBalance = getCurrentAccountBalance(acctID);
        return getNewBalanceAfterPayment(currentBalance, paymentAmount);
    }

    private void updateAccountAndPostProcessing(BillPayment payment, BigDecimal newBalance){
        AccountCode accountCode = getAccountCodeByPaymentCriteria(payment);
        int acctID = getAccountIDSegment(accountCode);

        BigDecimal currentBalance = getCurrentAccountBalance(acctID);
        postProcessingUpdate(newBalance, acctID, currentBalance);
    }



    private void updateBillPaymentHistory(final BillPaymentHistory billPaymentHistory){
        // Convert the model class to an entity
        BillPaymentHistoryEntity billPaymentHistoryEntity = billPaymentHistoryEntityBuilder.createEntity(billPaymentHistory);
        try {

            if(billPaymentHistoryEntity == null){
                throw new RuntimeException("Could not create BillPaymentHistoryEntity.");
            }
            billPaymentHistoryService.save(billPaymentHistoryEntity);

            Long id = billPaymentHistoryEntity.getPaymentHistoryID();
            billPaymentHistory.setPaymentId(id);

        }catch(Exception e){
            LOGGER.error("There was an error saving the BillPaymentHistory: {}" ,billPaymentHistoryEntity, e);
        }
    }

    private void scheduleNextPayment(BillPayment payment, TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap) {
        LocalDate nextPaymentDate = getNextPaymentDateFromPayment(payment);
        nextScheduledPaymentMap.put(nextPaymentDate, payment.getPaymentAmount());
    }

    private boolean isPaymentScheduleDateValid(BillPayment payment){
        return payment.getScheduledPaymentDate() != null;
    }

    private boolean isPaymentScheduleCriteriaValid(BillPayment payment){
        return (payment.getScheduledPaymentDate() != null || payment.getDueDate() != null);
    }

    private void validateBillPayment(BillPayment payment){
        if(payment == null){
            throw new InvalidBillPaymentException("Null Bill payment found.");
        }
    }

    //TODO: Implement Code
    @Override
    public ConfirmationNumber generateConfirmationNumberForBill(BillPayment billPayment) {
        return null;
    }

    //TODO: To implement 6/3/24
    @Override
    public void processRegularBillStatements(TreeMap<LocalDate, BillPayment> billPayments) {

    }

    @Override
    public void updatePaymentStatus(BillPayment billPayment) {
       billPayment.setScheduleStatus(ScheduleStatus.DONE);
       billPayment.setProcessed(true);
    }


    //TODO: To implement 6/3/24





    private void validateProcessedBillPayment(ProcessedBillPayment processedBillPayment, BillPaymentHistory billPaymentHistory) {
        if(!paymentVerification(processedBillPayment, billPaymentHistory)){
            throw new BillPaymentNotVerifiedException("Bill Payment not verified: " + processedBillPayment);
        }
    }

    private void validatePaymentDate(LocalDate paymentDate){
        if(paymentDate == null){
            throw new IllegalDateException("Found Null PaymentDate criteria: " + paymentDate);
        }
    }

    private void validatePaymentAmount(BigDecimal paymentAmount){
        if(paymentAmount == null){
            throw new NullPaymentAmountException("Found Null Payment Amount: " + paymentAmount);
        }
    }


    private ProcessedBillPayment buildProcessedBillPayment(LocalDate nextPaymentDate, BillPayment billPayment){
        return new ProcessedBillPayment(billPayment, true, LocalDate.now(), nextPaymentDate);
    }

    //TODO: To implement 6/3/24

    public TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment(BillPayment billPayment){
        TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap = new TreeMap<>();
        if(billPayment == null){
            throw new InvalidBillPaymentException("Unable to process null bill payment.");
        }

        if(!validatePaymentDatePriorDueDate(billPayment)){
            LateBillPayment lateBillPayment = buildLatePayment(billPayment);
            processLatePayment(lateBillPayment, nextScheduledPaymentMap);
        }else{
           processOnTimePayment(billPayment, nextScheduledPaymentMap);
        }

        return nextScheduledPaymentMap;
    }



    /**
     * This method will update the account balance, account details
     * @param newBalance
     * @param acctID
     */
    public void postProcessingUpdate(BigDecimal newBalance, int acctID, BigDecimal prevBalance){
        updateAccountBalance(newBalance, acctID);

        postProcessingUpdateBalanceHistory(newBalance, BigDecimal.ZERO, prevBalance, acctID);
    }

    private void updateIsProcessedParameter(BillPayment billPayment){
        billPayment.setProcessed(true);
    }

    public void postProcessingUpdateBalanceHistory(BigDecimal newBalance, BigDecimal adjusted, BigDecimal prevBalance, int acctID){
        // Create a balance history Model
        BalanceHistory balanceHistory = createBalanceHistoryModel(newBalance, adjusted, prevBalance, acctID);

        // Create a Balance History Entity
        BalanceHistoryEntity balanceHistoryEntity = balanceHistoryEntityBuilder.createEntity(balanceHistory);

        // Persist the Balance History
        balanceHistoryService.save(balanceHistoryEntity);
    }


    private BigDecimal getNewBalanceAfterPayment(BigDecimal currentBalance, BigDecimal amount){
        return currentBalance.subtract(amount);
    }


    private AccountCode getAccountCodeByPaymentCriteria(BillPayment billPayment){
        return billPayment.getAccountCode();
    }

    public int getAccountIDSegment(final AccountCode code){
        return code.getSequence();
    }



    private LocalDate getPaymentDate(BillPayment payment){
        return payment.getScheduledPaymentDate();
    }

    private LocalDate getPaymentDueDate(BillPayment payment){
        return payment.getDueDate();
    }

    private BigDecimal getPaymentAmountCriteria(final BillPayment billPayment){
        return billPayment.getPaymentAmount();
    }

    private boolean assertBillPaymentParameterNotNull(final BillPayment payment){
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
        return true;
    }

    private boolean assertBillPaymentParametersListNotNull(TreeMap<LocalDate, ? extends BillPayment> payments){
        for(BillPayment payment : payments.values()){
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
    public boolean sendProcessedPaymentNotification(ProcessedBillPayment billPayment) {
        return billPaymentNotificationSender.sendProcessedPaymentNotification(billPayment);
    }


    private void validateProcessedPayment(ProcessedBillPayment payment){
        if(payment == null){
            throw new InvalidProcessedBillPaymentException("Caught Null Processed Bill Payment: " + payment);
        }
    }

    @Override
    public void handleDailyPayments(TreeMap<BillPayment, BillPaymentSchedule> billPayments) {

    }

    @Override
    public void handleWeeklyPayments(TreeMap<BillPayment, BillPaymentSchedule> weeklyPayments) {

    }

    @Override
    public void handleMonthlyPayments(TreeMap<BillPayment, BillPaymentSchedule> monthlyPayments) {

    }

    @Override
    public void handleBiWeeklyPayments(TreeMap<BillPayment, BillPaymentSchedule> biWeeklyPayments) {

    }

    /**
     * Handles missed payments by returning a list of processed bill payments.
     *
     * @param lateBillPayments The list of late bill payments.
     * @return The list of processed bill payments.
     */
    @Override
    public List<ProcessedBillPayment> handleMissedPayments(List<LateBillPayment> lateBillPayments) {
        return List.of();
    }
}
