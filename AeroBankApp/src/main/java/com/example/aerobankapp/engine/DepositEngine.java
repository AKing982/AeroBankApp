package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.dto.MessageNotificationDTO;
import com.example.aerobankapp.dto.ProcessedDepositDTO;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.DepositBalanceSummary;
import com.example.aerobankapp.scheduler.SchedulerEngine;
import com.example.aerobankapp.scheduler.SchedulerEngineImpl;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.BalanceHistoryUtil;
import com.example.aerobankapp.workbench.utilities.DepositProcessorUtil;
import com.mchange.v2.collection.MapEntry;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.example.aerobankapp.workbench.utilities.DepositProcessorUtil.buildDepositBalanceSummary;

@Service
@Getter
@Setter
@PreAuthorize("isAuthenticated()")
public class DepositEngine extends TransactionEngine<Deposit, DepositBalanceSummary> implements Runnable {

    private final DepositService depositService;
    private final Logger LOGGER = LoggerFactory.getLogger(DepositEngine.class);

    @Autowired
    public DepositEngine(DepositService depositService,
                         AccountService accountService,
                         AccountSecurityService accountSecurityService,
                         NotificationService notificationService,
                         CalculationEngine calculationEngine,
                         BalanceHistoryService balanceHistoryService) {
        super(accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService);
        this.depositService = depositService;
    }


    @Override
    protected List<Deposit> fetchAll() {
        List<DepositsEntity> allDeposits = depositService.findAll();
        return allDeposits.stream()
                .map(DepositProcessorUtil::convertDepositEntityToDeposit)
                .toList();
    }

    @Override
    protected List<Deposit> retrieveTransactionsByUserID(int userID) {
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID Found: " + userID);
        }
        List<DepositsEntity> depositsEntities = depositService.findByUserID(userID);

        if(!depositsEntities.isEmpty()){
            return depositsEntities.stream()
                    .map(DepositProcessorUtil::convertDepositEntityToDeposit)
                    .toList();
        }else{
            throw new RuntimeException("No Deposits found.");
        }
    }

    @Override
    protected void sendNotification(List<DepositBalanceSummary> balanceSummaries) {

    }

    @Override
    protected Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(List<Deposit> transactionList) {
        Map<Integer, BigDecimal> accountBalanceMap = new HashMap<>();

        // Get the Set of AccountID's
        Set<Integer> accountIDs = retrieveAccountIDSet(transactionList);

        // Retrieve the current account balances
        Map<Integer, BigDecimal> currentAccountBalances = retrieveCurrentAccountBalancesByAcctID(accountIDs);

        // Retrieve the AccountID, Deposit Amount map
        Map<Integer, BigDecimal> depositAmountByAcctIDMap = retrieveTransactionAmountByAcctID(transactionList);

        // Calculate the new Account Balances
        for(Map.Entry<Integer, BigDecimal> entry : currentAccountBalances.entrySet()){
            for(Map.Entry<Integer, BigDecimal> depositAmountEntry : depositAmountByAcctIDMap.entrySet()){
                int acctID = entry.getKey();
                BigDecimal balance = entry.getValue();
                BigDecimal amount = depositAmountEntry.getValue();
                LOGGER.debug("Processing Balance: $" + balance);
                LOGGER.debug("Processing Amount: $" + amount);

                BigDecimal newBalance = getDepositCalculation(amount, balance);
                accountBalanceMap.put(acctID, newBalance);
            }
        }
        return accountBalanceMap;
    }

    @Override
    protected BigDecimal getCalculation(BigDecimal amount, BigDecimal balance) {
        return null;
    }

    @Override
    protected Set<Integer> retrieveAccountIDSet(List<Deposit> transactionList) {
        for(Deposit deposit : transactionList){
            int acctID = deposit.getAccountID();
            if(acctID > 0){
                accountIDSet.add(acctID);
            }else{
                throw new InvalidAccountIDException("Retrieving Invalid AcctID: " + acctID);
            }
        }
        return accountIDSet;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveTransactionAmountByAcctID(List<Deposit> transactionList) {
        if(!transactionList.isEmpty()){
            for(Deposit deposit : transactionList){
                final int acctID = deposit.getAccountID();
                final BigDecimal amount = deposit.getAmount();
                if(acctID > 0 && amount.compareTo(BigDecimal.ZERO) > 0){
                    transactionAmountByAcctIDHashMap.put(acctID, amount);
                }else{
                    throw new IllegalArgumentException("Invalid AcctID or Deposit Amount Found.");
                }
            }
            return transactionAmountByAcctIDHashMap;
        }else{
            throw new IllegalArgumentException("No Deposits found.");
        }
    }

    @Override
    protected Map<Integer, List<DepositBalanceSummary>> generateBalanceSummaryMap(List<Deposit> transactions, Map<Integer, BigDecimal> accountBalances) {
      assertTransactionListNotNull(transactions);
      assertAccountBalancesMapNotNull(accountBalances);
        for(Deposit deposit : transactions){
            if(deposit != null){
                int acctID = deposit.getAccountID();
                BigDecimal newBalance = accountBalances.get(acctID);
                if(newBalance == null){
                    throw new NullPointerException("Unable to retrieve new balance for accountID: " +  acctID);
                }
                DepositBalanceSummary balanceSummary = buildDepositBalanceSummary(deposit, newBalance);
                balanceSummariesHashMap.computeIfAbsent(acctID, k -> new ArrayList<>()).add(balanceSummary);
            }
        }
        return balanceSummariesHashMap;
    }

    @Override
    protected BalanceHistoryEntity createBalanceHistoryEntity(DepositBalanceSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount) {
        assertBalanceSummaryNotNull(balanceSummary);
        if(currentBalance == null || adjustedAmount == null){
            throw new IllegalArgumentException("Illegal Balance or Amount found.");
        }

        BalanceHistory balanceHistory = BalanceHistoryUtil.convertBalanceSummaryToBalanceHistoryModel(balanceSummary, currentBalance, adjustedAmount);
        return BalanceHistoryUtil.convertBalanceHistoryToEntity(balanceHistory);
    }

    @Override
    protected List<BalanceHistoryEntity> convertBalanceSummaryToBalanceHistoryEntityList(List<DepositBalanceSummary> transactionSummaries) {
        assertListNotEmpty(transactionSummaries);
        for(DepositBalanceSummary depositBalanceSummary : transactionSummaries){
            // Extract the Deposit
            Deposit deposit = depositBalanceSummary.getTransaction();
            if(deposit == null) {
                throw new InvalidDepositException("No Deposits have been retrieved from the balance summary.");
            }
            BigDecimal originalBalance = getCurrentBalanceByAcctID(deposit.getAccountID());
            if(originalBalance == null){
                throw new InvalidBalanceException("Could not find Starting balance.");
            }
            LOGGER.info("Balance before deposit: $" + originalBalance);
            BigDecimal balanceAfterDeposit = depositBalanceSummary.getPostBalance();
            LOGGER.info("Balance After Deposit: $" + balanceAfterDeposit);
            BigDecimal adjustedAmount = getAdjustedAmount(originalBalance, balanceAfterDeposit);
            LOGGER.info("Adjusted Amount: " + adjustedAmount);

            BalanceHistoryEntity balanceHistoryEntity = createBalanceHistoryEntity(depositBalanceSummary, originalBalance, adjustedAmount);
            balanceHistoryList.add(balanceHistoryEntity);

            if(balanceAfterDeposit == null){
                throw new InvalidBalanceException("Retrieving Invalid Balance from Balance Summary...");
            }
        }
        return balanceHistoryList;
    }

    public BigDecimal getDepositCalculation(final BigDecimal amount, final BigDecimal balance)
    {
        if(amount == null || balance == null){
            throw new IllegalArgumentException("Unable to calculate deposit from null amount or balance");
        }
        if(amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(BigDecimal.ZERO) > 0){
            return getCalculationEngine().calculateDeposit(amount, balance);
        }
        else if(balance.equals(BigDecimal.ZERO)){
            throw new ZeroBalanceException("Invalid Balance Found.");
        }
        else if(amount.equals(BigDecimal.ZERO)){
            return balance;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void run() {
        try {
            // Step 1: Fetch all deposits
            List<Deposit> allDeposits = fetchAll();
            if(allDeposits.isEmpty()){
                LOGGER.info("No deposits to process.");
                return;
            }
            // Step 2: Get calculated account balances based on the deposits
            Map<Integer, BigDecimal> calculatedAccountBalances = getCalculatedAccountBalanceMap(allDeposits);

            // Step 3: Generate deposit balance summaries for notification or further processing
            Map<Integer, List<DepositBalanceSummary>> depositBalanceSummaryMap = generateBalanceSummaryMap(allDeposits, calculatedAccountBalances);

            // Step 4: Convert deposit summaries to balance history entities
            List<BalanceHistoryEntity> balanceHistoryEntities = new ArrayList<>();
            depositBalanceSummaryMap.forEach((acctID, depositBalanceSummaries) ->
                    balanceHistoryEntities.addAll(convertBalanceSummaryToBalanceHistoryEntityList(depositBalanceSummaries)));

            // Step 5: Add balance histories to the database
            addBalanceHistoriesToDatabase(balanceHistoryEntities);

            // Step 6: Optionally, send notifications about the deposits
            // This step depends on your application's requirements.
            depositBalanceSummaryMap.values().forEach(this::sendNotification);

            LOGGER.info("Deposit processing completed successfully.");
        } catch(Exception e) {
            LOGGER.error("Error occurred during deposit processing: " + e.getMessage(), e);
        }
    }
}
