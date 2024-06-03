package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.AccountDetailsEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
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
                                 AccountService accountService,
                                 AccountDetailsService accountDetailsService,
                                 BalanceHistoryService balanceHistoryService,
                                 @Qualifier("accountDetailsEntityBuilderImpl") EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder,
                                 @Qualifier("balanceHistoryEntityBuilderImpl") EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder){
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentService = billPaymentService;
        this.billPaymentNotificationService = billPaymentNotificationService;
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

        LOGGER.info("Validating Payment Criteria.");
        if(paymentCriteriaVerified(processedBillPayment)){
            // Validate that the payment has been saved to the database
            Long paymentID = getProcessedBillPaymentID(processedBillPayment);
            Optional<BillPaymentEntity> billPaymentEntityOptional = fetchBillPaymentFromDB(paymentID);
            LOGGER.info("Validating payment is Present in the database");
            if(paymentIsPresent(billPaymentEntityOptional)){
                // Is the isProcessed field set to true
                LOGGER.info("Validating that the payment is processed");
                if(isPaymentProcessed(paymentID)){
                    return true;
                }
            }
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
        return billPaymentService.isBillPaymentProcessed(id);
    }

    private Optional<BillPaymentEntity> fetchBillPaymentFromDB(final Long paymentID){
        return billPaymentService.findById(paymentID);
    }

    private boolean paymentCriteriaVerified(final ProcessedBillPayment payment){
        return (payment.isComplete() && payment.getLastProcessedDate() != null);
    }

    private boolean paymentIsPresent(final Optional<BillPaymentEntity> billPaymentEntityOptional){
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
    public ScheduleStatus updatePaymentStatus(BillPayment billPayment) {
        return null;
    }

    //TODO: To implement 6/3/24
    @Override
    public void processLatePayments(TreeMap<LocalDate, LateBillPayment> billPayments) {

    }

    //TODO: Implement code
    @Override
    public void processLateFeesForLatePayments(TreeMap<LocalDate, BillPayment> latePayments) {

    }

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
        if(!assertBillPaymentParameterNotNull(billPayment)) {
            throw new InvalidBillPaymentParametersException("Unable to process payment due to invalid parameters.");
        }
        return null;
       // return getProcessedPayment(billPayment);
    }

    //TODO: To implement 6/3/24

    public ProcessedBillPayment getProcessedPayment(BillPayment billPayment){
        ProcessedBillPayment processedBillPayment = null;
        if(billPayment == null){
            throw new InvalidBillPaymentException("Unable to process null bill payment.");
        }
        if(validatePaymentDatePriorDueDate(billPayment)){
            // Retrieve the payment Amount
            BigDecimal paymentAmount = getPaymentAmountCriteria(billPayment);

            // Retrieve the accountCode
            AccountCode accountCode = getAccountCodeByPaymentCriteria(billPayment);

            // Retrieve the account segment
            int acctSegment = getAccountIDSegment(accountCode);

            // Retrieve the balance
            BigDecimal currentBalance = getCurrentAccountBalance(acctSegment);

            // Deduct the payment from the account balance
            BigDecimal newBalance = getNewBalanceAfterPayment(currentBalance, paymentAmount);

            postProcessingUpdate(newBalance, acctSegment, currentBalance);

        }
        return null;
    }

    /**
     * This method will update the account balance, account details
     * @param newBalance
     * @param acctID
     */
    public void postProcessingUpdate(BigDecimal newBalance, int acctID, BigDecimal prevBalance){
        updateAccountBalance(newBalance, acctID);

       // postProcessingUpdateBalanceHistory();
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
