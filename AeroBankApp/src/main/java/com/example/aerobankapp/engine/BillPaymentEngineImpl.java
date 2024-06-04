package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.services.builder.AccountDetailsEntityBuilderImpl;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BillPaymentEngineImpl implements BillPaymentEngine
{
    private final BillPaymentScheduleService billPaymentScheduleService;
    private final BillPaymentService billPaymentService;
    private final BillPaymentNotificationService billPaymentNotificationService;
    private final BillPaymentHistoryService billPaymentHistoryService;
    private final AccountService accountService;
    private final AccountDetailsService accountDetailsService;
    private final BalanceHistoryService balanceHistoryService;
    private final EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder;
    private final EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder;
    private PendingBalanceCalculatorEngineImpl pendingBalanceCalculatorEngine;
    private TreeMap<LocalDate, AutoPayBillPayment> futureAutoPayments = new TreeMap<>();
    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentEngineImpl.class);

    @Autowired
    public BillPaymentEngineImpl(
                                 BillPaymentScheduleService billPaymentScheduleService,
                                 BillPaymentService billPaymentService,
                                 BillPaymentNotificationService billPaymentNotificationService,
                                 BillPaymentHistoryService billPaymentHistoryService,
                                 AccountService accountService,
                                 AccountDetailsService accountDetailsService,
                                 BalanceHistoryService balanceHistoryService,
                                 @Qualifier("accountDetailsEntityBuilderImpl") EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder,
                                 @Qualifier("balanceHistoryEntityBuilderImpl") EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder){
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentService = billPaymentService;
        this.billPaymentNotificationService = billPaymentNotificationService;
        this.billPaymentHistoryService = billPaymentHistoryService;
        this.accountService = accountService;
        this.accountDetailsService = accountDetailsService;
        this.balanceHistoryService = balanceHistoryService;
        this.accountDetailsEntityBuilder = accountDetailsEntityBuilder;
        this.balanceHistoryEntityBuilder = balanceHistoryEntityBuilder;
    }

    //TODO: To Implement 6/3/24

    @Override
    public List<ProcessedBillPayment> autoPayBills(final List<BillPayment> billPayments) {
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
            if(!assertBillPaymentNull(billPayment)){
                if(billPayment.isAutoPayEnabled()){
                    // Process the bill payment
                    ProcessedBillPayment processedBillPayment = processSinglePayment(billPayment);

                    // Send the notification to the user

                    // Build the next scheduled payment date
                }
            }else{
                continue;
            }
        }
        // Does the bill payment have auto pay enabled?
        // If auto pay is enabled, proceed to process the payment
        // Once the payment has been processed, send a notification to the user/account
        // build the next scheduled payment date
        return new ArrayList<>();
    }

    private boolean assertPaymentsSizeEqualToOne(List<BillPayment> billPayments){
        return billPayments.size() == 1;
    }

    private boolean assertBillPaymentNull(BillPayment billPayment){
        return billPayment == null;
    }

    @Override
    public boolean paymentVerification(final ProcessedBillPayment processedBillPayment) {
        if(processedBillPayment == null){
            throw new InvalidProcessedBillPaymentException("Processed bill payment cannot be null.");
        }

        if(!paymentCriteriaVerified(processedBillPayment)){
            return false;
        }

        LOGGER.info("Validating Payment Criteria.");
        try{
            Long paymentID = getProcessedBillPaymentID(processedBillPayment);
            LOGGER.info("Validating PaymentID: {}", paymentID);
            Optional<BillPaymentHistoryEntity> billPaymentEntityOptional = fetchBillPaymentFromDB(paymentID);
            LOGGER.info("Validating payment is Present in the database");
            if(paymentIsPresent(billPaymentEntityOptional)){
                    // Is the isProcessed field set to true
                LOGGER.info("Validating that the payment is processed");
                if(isPaymentProcessed(paymentID)){
                    return true;
                }
            }
            }catch(Exception e){
                LOGGER.error("Error while processing the payment", e);
                return false;
        }
        return false;
    }

    public LocalDate getNextPaymentDateFromPayment(BillPayment payment){
        if(payment == null){
            throw new InvalidBillPaymentException("Unable to retrieve next payment date from null bill payment.");
        }
        LocalDate dueDate = payment.getDueDate();
        ScheduleFrequency frequency = payment.getScheduleFrequency();
        LocalDate paymentDate = payment.getScheduledPaymentDate();

        if(paymentDate == null){
            return calculateNextPaymentDate(dueDate, frequency);
        }
        return calculateNextPaymentDate(paymentDate, frequency);
    }



    @Override
    public LocalDate calculateNextPaymentDate(LocalDate currentDate, ScheduleFrequency frequency) {
        validateNextPaymentDateCriteria(currentDate, frequency);
        switch(frequency){
            case MONTHLY -> {
                return buildNextPaymentDateByMonth(currentDate);
            }
            case WEEKLY -> {
                return buildNextPaymentDateByWeek(currentDate);
            }
            case BI_WEEKLY -> {
                return buildNextPaymentDateByBiWeekly(currentDate);
            }
        }
        return currentDate;
    }

    private LocalDate buildNextPaymentDateByBiWeekly(LocalDate date){
        if(date != null){
            return date.plusWeeks(2);
        }
        throw new IllegalDateException("Unable to build next payment date by month from null date.");
    }

    private LocalDate buildNextPaymentDateByWeek(LocalDate date){
        if(date != null){
            return date.plusWeeks(1);
        }
        throw new IllegalDateException("Unable to build next payment date by month from null date.");
    }

    private LocalDate buildNextPaymentDateByMonth(LocalDate date){
        if(date != null){
            return date.plusMonths(1);
        }
        throw new IllegalDateException("Unable to build next payment date by month from null date.");
    }

    private void validateNextPaymentDateCriteria(LocalDate date, ScheduleFrequency frequency){
        if(date == null){
            throw new IllegalDateException("Null Date criteria caught. Unable to calculate next payment date.");
        }
        else if(frequency == null){
            throw new IllegalScheduleCriteriaException("Null ScheduleFrequency found. Unable to calculate next payment date.");
        }
    }

    private boolean isPaymentProcessed(Long id){
        return billPaymentHistoryService.isPaymentProcessedById(id);
    }

    private Optional<BillPaymentHistoryEntity> fetchBillPaymentFromDB(final Long paymentID){
        return billPaymentHistoryService.findAllById(paymentID);
    }

    private boolean paymentCriteriaVerified(final ProcessedBillPayment payment){
        return (payment.isComplete() && payment.getLastProcessedDate() != null);
    }

    private boolean paymentIsPresent(final Optional<BillPaymentHistoryEntity> billPaymentEntityOptional){
        return billPaymentEntityOptional.isPresent();
    }

    private Long getProcessedBillPaymentID(final ProcessedBillPayment payment){
        return payment.getBillPayment().getPaymentID();
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
        validateBillPayment(billPayment);
        if(!isPaymentScheduleCriteriaValid(billPayment)){
            throw new InvalidDateException("Unable to retrieve last payment date from null payment schedule criteria.");
        }
        else if(!isDueDateValid(billPayment)){
            return billPayment.getScheduledPaymentDate();
        }
        else if(!isPaymentScheduleDateValid(billPayment)) {
            return billPayment.getDueDate();
        }
        return billPayment.getScheduledPaymentDate();
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

    @Override
    public LateBillPayment buildLatePayment(BillPayment payment) {
        if(payment == null){
            throw new InvalidBillPaymentException("Found null payment criteria.");
        }
        final BigDecimal lateFee = new BigDecimal("25.00");
        return new LateBillPayment(payment.getDueDate(), lateFee, payment);
    }

    @Override
    public void processLatePayment(LateBillPayment lateBillPayment, TreeMap<LocalDate, BigDecimal> nextScheduledPaymentMap) {
        BigDecimal lateFee = lateBillPayment.getLateFee();

        BigDecimal newPaymentAmount = lateBillPayment.getBillPayment().getPaymentAmount().add(lateFee);
        LocalDate nextPaymentDate = getNextPaymentDateFromPayment(lateBillPayment.getBillPayment());
        nextScheduledPaymentMap.put(nextPaymentDate, newPaymentAmount);
    }

    //TODO: To implement 6/3/24


    @Override
    public boolean sendLatePaymentNotification() {
        return false;
    }

    //TODO: To implement 6/3/24
    @Override
    public TreeMap<LocalDate, ProcessedBillPayment> processPayments(TreeMap<LocalDate, ? extends BillPayment> payments) {
        if(payments.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to process payments from empty list.");
        }
        if(!assertBillPaymentParametersListNotNull(payments)){
            throw new InvalidBillPaymentParametersException("Unable to process payments due to null bill payments.");
        }

        return null;
    }

    //TODO: To implement 6/3/24
    @Override
    public ProcessedBillPayment processSinglePayment(final BillPayment billPayment) {
        BigDecimal paymentAmount = billPayment.getPaymentAmount();
        validatePaymentAmount(paymentAmount);

        LocalDate paymentDate = billPayment.getScheduledPaymentDate();
        validatePaymentDate(paymentDate);

        TreeMap<LocalDate, BigDecimal> nextScheduledPayment = processPaymentAndScheduleNextPayment(billPayment);
        // Get the current Account Balance
        LocalDate nextPaymentDate = nextScheduledPayment.firstKey();

        ProcessedBillPayment processedBillPayment = buildProcessedBillPayment(nextPaymentDate, billPayment);

        // Validate the Processed Bill Payment
        validateProcessedBillPayment(processedBillPayment);

        return processedBillPayment;
    }

    private void validateProcessedBillPayment(ProcessedBillPayment processedBillPayment) {
        if(!paymentVerification(processedBillPayment)){
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


    private BalanceHistory createBalanceHistoryModel(BigDecimal newBalance, BigDecimal adjusted, BigDecimal prevBalance, int acctID){
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setNewBalance(newBalance);
        balanceHistory.setCurrentBalance(prevBalance);
        balanceHistory.setAdjustedAmount(adjusted);
        balanceHistory.setAccountID(acctID);
        return balanceHistory;
    }

    private void updateAccountBalance(BigDecimal newBalance, int acctID){
        accountService.updateAccountBalanceByAcctID(newBalance, acctID);
    }

    private BigDecimal getNewBalanceAfterPayment(BigDecimal currentBalance, BigDecimal amount){
        return currentBalance.subtract(amount);
    }

    private BigDecimal getCurrentAccountBalance(int acctID){
        BigDecimal balance = accountService.getBalanceByAcctID(acctID);
        if(balance == null){
            LOGGER.error("Unable to retrieve balances for accountID: {}", acctID);
        }
        return balance;
    }

    private AccountCode getAccountCodeByPaymentCriteria(BillPayment billPayment){
        return billPayment.getAccountCode();
    }

    public int getAccountIDSegment(final AccountCode code){
        return code.getSequence();
    }

    public boolean validatePaymentDatePriorDueDate(BillPayment payment){
        LocalDate paymentDate = getPaymentDate(payment);
        LocalDate dueDate = getPaymentDueDate(payment);
        if(paymentDate == null || dueDate == null){
            LOGGER.error("There was an error validating the Payment Date due to null date parameters: Payment Date: {}, Due Date: {}", paymentDate, dueDate);
            throw new InvalidDateException("Invalid Payment Date found");
        }
        return paymentDate.isBefore(dueDate);
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
}
