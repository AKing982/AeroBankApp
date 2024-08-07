package com.example.aerobankapp.engine;

import com.example.aerobankapp.converter.EntityToModelConverter;
import com.example.aerobankapp.converter.WithdrawConverter;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.WithdrawBalanceSummary;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.utilities.BalanceHistoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.aerobankapp.engine.TransactionEngineUtil.buildWithdrawBalanceSummary;

@Service
public class WithdrawEngine extends TransactionEngine<Withdraw, WithdrawBalanceSummary> implements Runnable
{
    private final WithdrawService withdrawService;
    private final EntityToModelConverter<WithdrawEntity, Withdraw> withdrawConverter;
    private final Logger LOGGER = LoggerFactory.getLogger(WithdrawEngine.class);

    @Autowired
    public WithdrawEngine(WithdrawService withdrawService, UserService userService, AccountService accountService, AccountNotificationService accountNotificationService, AccountSecurityService accountSecurityService, NotificationService notificationService, CalculationEngine calculationEngine, BalanceHistoryService balanceHistoryService, EncryptionService encryptionService) {
        super(accountService, accountNotificationService, userService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
        this.withdrawService = withdrawService;
        this.withdrawConverter = new WithdrawConverter();
    }

    @Override
    protected List<Withdraw> fetchAll() {
        List<WithdrawEntity> withdrawEntities = withdrawService.findAll();
        return withdrawEntities.stream()
                .map(withdrawConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    protected PriorityQueue<Withdraw> fetchAllWithPriority() {
        return null;
    }

    @Override
    protected List<Withdraw> retrieveTransactionsByUserID(int userID) {
        return null;
    }

    @Override
    protected void sendNotification(List<WithdrawBalanceSummary> balanceSummaries) {

    }

    @Override
    protected Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(List<Withdraw> transactionList) {

        Map<Integer, BigDecimal> accountBalanceMap = new HashMap<>();

        // Retrieve the AccountID's for each Withdraw
        Set<Integer> accountIds = retrieveAccountIDSet(transactionList);

        // Get the Current Account Balances
        Map<Integer, BigDecimal> currentAccountBalances = retrieveCurrentAccountBalancesByAcctID(accountIds);

        // Get the Withdraw amount related to the account ID
        Map<Integer, BigDecimal> withdrawAmountMap = retrieveTransactionAmountByAcctID(transactionList);

        for(Map.Entry<Integer, BigDecimal> entry : currentAccountBalances.entrySet()){
            for(Map.Entry<Integer, BigDecimal> withdrawAmountEntry : withdrawAmountMap.entrySet()){
                int fromAccountID = entry.getKey();
                BigDecimal balance = entry.getValue();
                BigDecimal amount = withdrawAmountEntry.getValue();
                LOGGER.debug("Processing Balance: $" + balance);
                LOGGER.debug("Processing Amount: $" + amount);

                BigDecimal newBalance = getWithdrawCalculation(amount, balance);
                accountBalanceMap.put(fromAccountID, newBalance);
            }
        }
        return accountBalanceMap;
    }

    protected BigDecimal getWithdrawCalculation(final BigDecimal amount, final BigDecimal balance){
        if(amount != null || balance != null){
            if(amount.compareTo(BigDecimal.ZERO) > 0){
                LOGGER.debug("Receiving Balance: $" + balance);
                LOGGER.debug("Receiving Amount: $" + amount);
                BigDecimal balanceAfterWithdrawal = getCalculationEngine().calculateWithdrawal(amount, balance);
                LOGGER.debug("New Balance after Withdrawal: $" + balanceAfterWithdrawal);
                return balanceAfterWithdrawal;
            }
        }
        return null;
    }


    @Override
    protected Set<Integer> retrieveAccountIDSet(List<Withdraw> transactionList) {
        for(Withdraw withdraw : transactionList){
            if(withdraw != null){
                int fromAccountID = withdraw.getFromAccountID();
                if(fromAccountID > 0){
                    accountIDSet.add(fromAccountID);
                }
            }
        }
        return accountIDSet;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveTransactionAmountByAcctID(final List<Withdraw> transactionList) {
        if(!transactionList.isEmpty()){
            for(Withdraw withdraw : transactionList){
                if(withdraw != null){
                    final int fromAccountID = withdraw.getFromAccountID();
                    final BigDecimal amount = withdraw.getAmount();
                    LOGGER.debug("Amount: $" + amount);
                    if(fromAccountID > 0 || amount.compareTo(BigDecimal.ZERO) > 0){
                        transactionAmountByAcctIDHashMap.put(fromAccountID, amount);
                    }
                }
            }
            return transactionAmountByAcctIDHashMap;
        }
        return null;
    }

    @Override
    protected Map<Integer, List<WithdrawBalanceSummary>> generateBalanceSummaryMap(List<Withdraw> transactions, Map<Integer, BigDecimal> accountBalances) {
        assertTransactionListNotNull(transactions);
        assertAccountBalancesMapNotNull(accountBalances);
        for(Withdraw withdraw : transactions){
            if(withdraw != null){
                int fromAccountID = withdraw.getFromAccountID();
                if(fromAccountID > 0){
                    BigDecimal newBalance = accountBalances.get(fromAccountID);
                    if(newBalance != null){
                        WithdrawBalanceSummary withdrawBalanceSummary = buildWithdrawBalanceSummary(withdraw, newBalance);
                        balanceSummariesHashMap.computeIfAbsent(fromAccountID, k -> new ArrayList<>()).add(withdrawBalanceSummary);
                    }
                }
            }
        }
        return balanceSummariesHashMap;
    }

    @Override
    protected BalanceHistoryEntity createBalanceHistoryEntity(WithdrawBalanceSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount) {
        assertBalanceSummaryNotNull(balanceSummary);
        if(currentBalance != null || adjustedAmount != null){
            BalanceHistory balanceHistory = BalanceHistoryUtil.convertWithdrawBalanceSummaryToBalanceHistory(balanceSummary, currentBalance, adjustedAmount);
            return BalanceHistoryUtil.convertBalanceHistoryToEntity(balanceHistory);
        }else {
            throw new RuntimeException("Unable to create Balance History");
        }
    }

    @Override
    protected List<BalanceHistoryEntity> convertBalanceSummaryToBalanceHistoryEntityList(List<WithdrawBalanceSummary> transactionSummaries) {
       assertListNotEmpty(transactionSummaries);
       for(WithdrawBalanceSummary withdrawBalanceSummary : transactionSummaries){
           Withdraw withdraw = withdrawBalanceSummary.getTransaction();
           if(withdraw != null){
               BigDecimal originalBalance = getCurrentBalanceByAcctID(withdraw.getFromAccountID());
               LOGGER.debug("Balance before withdrawal: $" + originalBalance);
               BigDecimal balanceAfterWithdrawal = withdrawBalanceSummary.getPostBalance();
               LOGGER.debug("Balance After Withdrawal: $" + balanceAfterWithdrawal);
           }
       }
        return null;
    }

    @Override
    public void run() {
        try {
            // Fetch all pending withdrawal transactions
            List<Withdraw> withdrawals = fetchAll();

            if (withdrawals.isEmpty()) {
                LOGGER.info("No withdrawal transactions to process.");
                return;
            }

            // Retrieve transactions by user ID if needed or process all fetched transactions
            // List<Withdraw> withdrawalsByUserID = retrieveTransactionsByUserID(userID); // If user-specific processing is required

            // Calculate the new account balances after the withdrawals
            Map<Integer, BigDecimal> calculatedAccountBalanceMap = getCalculatedAccountBalanceMap(withdrawals);

            // Generate balance summaries for each processed withdrawal
            Map<Integer, List<WithdrawBalanceSummary>> balanceSummaryMap = generateBalanceSummaryMap(withdrawals, calculatedAccountBalanceMap);

            bulkUpdateAccountBalances(calculatedAccountBalanceMap);

            // For each account, update its balance in the database and create balance history entities
            for (Map.Entry<Integer, BigDecimal> entry : calculatedAccountBalanceMap.entrySet()) {
                int accountId = entry.getKey();
                BigDecimal newBalance = entry.getValue();

                // Create balance history entities for each withdrawal and persist them
                List<WithdrawBalanceSummary> summaries = balanceSummaryMap.get(accountId);
                List<BalanceHistoryEntity> balanceHistoryEntities = summaries.stream()
                        .map(summary -> createBalanceHistoryEntity(summary, newBalance, summary.getTransaction().getAmount()))
                        .collect(Collectors.toList());

                addBalanceHistoriesToDatabase(balanceHistoryEntities);
            }

            // Optionally, send notifications to users about their processed withdrawals
            List<WithdrawBalanceSummary> allSummaries = new ArrayList<>();
            balanceSummaryMap.values().forEach(allSummaries::addAll);
            sendNotification(allSummaries);

            LOGGER.info("Successfully processed " + withdrawals.size() + " withdrawal transactions.");
        } catch (Exception e) {
            LOGGER.error("Error processing withdrawals: " + e.getMessage(), e);
        }
    }
}
