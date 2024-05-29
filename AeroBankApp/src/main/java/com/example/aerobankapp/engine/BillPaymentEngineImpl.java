package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.AccountDetailsEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.services.builder.AccountDetailsEntityBuilderImpl;
import com.example.aerobankapp.services.builder.EntityBuilder;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BillPaymentEngineImpl implements BillPaymentEngine
{
    private final RabbitTemplate rabbitTemplate;
    private final BillPaymentScheduleService billPaymentScheduleService;
    private final BillPaymentService billPaymentService;
    private final BillPaymentNotificationService billPaymentNotificationService;
    private final AccountService accountService;
    private final AccountDetailsService accountDetailsService;
    private final BalanceHistoryService balanceHistoryService;
    private final EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder;
    private final EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder;
    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentEngineImpl.class);

    @Autowired
    public BillPaymentEngineImpl(RabbitTemplate rabbitTemplate,
                                 BillPaymentScheduleService billPaymentScheduleService,
                                 BillPaymentService billPaymentService,
                                 BillPaymentNotificationService billPaymentNotificationService,
                                 AccountService accountService,
                                 AccountDetailsService accountDetailsService,
                                 BalanceHistoryService balanceHistoryService,
                                 @Qualifier("accountDetailsEntityBuilderImpl") EntityBuilder<AccountDetailsEntity, AccountDetails> accountDetailsEntityBuilder,
                                 @Qualifier("balanceHistoryEntityBuilderImpl") EntityBuilder<BalanceHistoryEntity, BalanceHistory> balanceHistoryEntityBuilder){
        this.rabbitTemplate = rabbitTemplate;
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentService = billPaymentService;
        this.billPaymentNotificationService = billPaymentNotificationService;
        this.accountService = accountService;
        this.accountDetailsService = accountDetailsService;
        this.balanceHistoryService = balanceHistoryService;
        this.accountDetailsEntityBuilder = accountDetailsEntityBuilder;
        this.balanceHistoryEntityBuilder = balanceHistoryEntityBuilder;
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
        if(!assertBillPaymentParametersListNotNull(payments)){
            throw new InvalidBillPaymentParametersException("Unable to process payments due to null bill payments.");
        }

        return null;
    }

    @Override
    public ProcessedBillPayment processSinglePayment(final BillPayment billPayment) {
        if(!assertBillPaymentParameterNotNull(billPayment)) {
            throw new InvalidBillPaymentParametersException("Unable to process payment due to invalid parameters.");
        }
        return getProcessedPayment(billPayment);
    }

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
            BigDecimal acctBalance = getCurrentAccountBalance(acctSegment);

            // Deduct the payment from the account balance
            BigDecimal newBalance = getNewBalanceAfterPayment(acctBalance, paymentAmount);

            postProcessingUpdate(newBalance, acctSegment);

        }
        return null;
    }

    /**
     * This method will update the account balance, account details
     * @param newBalance
     * @param acctID
     */
    public void postProcessingUpdate(BigDecimal newBalance, int acctID){
        updateAccountBalance(newBalance, acctID);

        postProcessingUpdateBalanceHistory();
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

    private boolean assertBillPaymentParametersListNotNull(List<? extends BillPayment> payments){
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
