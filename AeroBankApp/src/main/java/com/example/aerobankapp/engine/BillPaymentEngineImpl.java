package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.services.builder.AccountDetailsEntityBuilderImpl;
import com.example.aerobankapp.services.builder.AccountNotificationEntityBuilderImpl;
import com.example.aerobankapp.services.builder.BillPaymentHistoryEntityBuilderImpl;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.AccountNotificationCategory;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final AccountNotificationService accountNotificationService;
    private final BalanceHistoryService balanceHistoryService;
    private final EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder;
    private final EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder;
    private EntityBuilder<BillPaymentHistoryEntity, BillPaymentHistory> billPaymentHistoryEntityBuilder;
    private EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityBuilder;
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
                                 AccountNotificationService accountNotificationService,
                                 BalanceHistoryService balanceHistoryService,
                                 @Qualifier("accountDetailsEntityBuilderImpl") EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder,
                                 @Qualifier("balanceHistoryEntityBuilderImpl") EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder,
                                 EntityBuilder<BillPaymentHistoryEntity, BillPaymentHistory> billPaymentHistoryEntityBuilder,
                                 EntityBuilder<AccountNotificationEntity, AccountNotification> accountNotificationEntityAccountNotificationEntityBuilder){
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentService = billPaymentService;
        this.billPaymentNotificationService = billPaymentNotificationService;
        this.billPaymentHistoryService = billPaymentHistoryService;
        this.accountService = accountService;
        this.accountNotificationService = accountNotificationService;
        this.balanceHistoryService = balanceHistoryService;
        this.accountDetailsEntityBuilder = accountDetailsEntityBuilder;
        this.balanceHistoryEntityBuilder = balanceHistoryEntityBuilder;
        this.billPaymentHistoryEntityBuilder = new BillPaymentHistoryEntityBuilderImpl();
        this.accountNotificationEntityBuilder = new AccountNotificationEntityBuilderImpl();
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

    private BillPaymentHistory createBillPaymentHistoryModel(ProcessedBillPayment processedBillPayment){
        return new BillPaymentHistory(processedBillPayment.getNextPaymentDate(), processedBillPayment.getLastProcessedDate(), LocalDate.now(), true);
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
    @Deprecated
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
        LOGGER.info("Processed Bill Payment: {}", processedBillPayment);

        BillPaymentHistory billPaymentHistory = createBillPaymentHistoryModel(processedBillPayment);

        updateBillPaymentHistory(billPaymentHistory);

        // Validate the Processed Bill Payment
        validateProcessedBillPayment(processedBillPayment, billPaymentHistory);

        return processedBillPayment;
    }

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
    public boolean sendProcessedPaymentNotification(ProcessedBillPayment billPayment) {
        validateProcessedPayment(billPayment);

        // Build the notification String
        StringBuilder notificationMessage = buildProcessedPaymentNotification(billPayment);

        // Create an AccountNotification model
        int acctID = getAccountIDSegment(billPayment.getBillPayment().getAccountCode());
        AccountNotification accountNotification = buildAccountNotificationModel(notificationMessage, billPayment.getBillPayment().getPayeeName(), acctID);

        // Convert the AccountNotificationModel to entity
        AccountNotificationEntity accountNotificationEntity = accountNotificationEntityBuilder.createEntity(accountNotification);
        if(accountNotificationEntity == null){
            throw new IllegalArgumentException("Unable to build AccountNotificationEntity");
        }
        try
        {
            // Save the AccountNotification Entity
            accountNotificationService.save(accountNotificationEntity);
            return true;

        }catch(Exception e){
            LOGGER.error("There was an error saving the Account Notification to the server: {}", accountNotificationEntity, e);
            return false;
        }
    }

    private AccountNotification buildAccountNotificationModel(StringBuilder strMessage, String payeeName, int acctID){
        return AccountNotification.builder()
                .accountID(acctID)
                .title(payeeName)
                .message(strMessage.toString())
                .category(AccountNotificationCategory.PAYMENT_PROCESSED)
                .isRead(false)
                .isSevere(false)
                .priority(1)
                .build();
    }

    private StringBuilder buildProcessedPaymentNotification(ProcessedBillPayment payment){
        StringBuilder sb = new StringBuilder();
        int acctID = payment.getBillPayment().getAccountCode().getSequence();
        BigDecimal paymentAmount = payment.getBillPayment().getPaymentAmount();
        LocalDate nextPaymentDate = payment.getNextPaymentDate();
        sb.append("Payment of $")
                .append(paymentAmount)
                .append(" has been processed for account ")
                .append(acctID)
                .append(". Next payment is scheduled on ")
                .append(nextPaymentDateAsString(nextPaymentDate))
                .append(".");
        return sb;
    }

    private String nextPaymentDateAsString(LocalDate date){
        return (date != null) ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : "N/A";
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
