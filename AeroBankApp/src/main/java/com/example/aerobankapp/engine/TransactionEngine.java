package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.AbstractTransactionEntityModel;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.exceptions.InvalidDepositException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.ZeroBalanceException;
import com.example.aerobankapp.model.TransactionBalanceSummary;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.CalculationStrategy;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
public abstract class TransactionEngine<T extends TransactionBase, S extends TransactionBalanceSummary<T>>
{
    private final AccountService accountService;
    private final UserService userService;
    private final AccountSecurityService accountSecurityService;
    private final NotificationService notificationService;
    private final CalculationEngine calculationEngine;
    private final BalanceHistoryService balanceHistoryService;
    private final EncryptionService encryptionService;
    protected Set<Integer> accountIDSet = new HashSet<>();
    protected int successCount = 0;
    protected List<BalanceHistoryEntity> balanceHistoryList = new ArrayList<>();
    protected Map<Integer, List<S>> balanceSummariesHashMap = new HashMap<>();
    protected Map<Integer, BigDecimal> transactionAmountByAcctIDHashMap = new HashMap<>();
    private Logger LOGGER = LoggerFactory.getLogger(TransactionEngine.class);

    public TransactionEngine(AccountService accountService,
                             UserService userService,
                             AccountSecurityService accountSecurityService,
                             NotificationService notificationService,
                             CalculationEngine calculationEngine,
                             BalanceHistoryService balanceHistoryService,
                             EncryptionService encryptionService){
        this.accountService = accountService;
        this.userService = userService;
        this.accountSecurityService = accountSecurityService;
        this.notificationService = notificationService;
        this.calculationEngine = calculationEngine;
        this.balanceHistoryService = balanceHistoryService;
        this.encryptionService = encryptionService;
    }

    protected abstract List<T> fetchAll();

    protected void updateBalanceForAccountID(BigDecimal balance, int acctID){
        if(acctID > 0 && balance.compareTo(BigDecimal.ZERO) > 0){
            accountService.updateAccountBalanceByAcctID(balance, acctID);
        }
    }

    private BigDecimal getMinimumBalanceRequirements(final int acctID){
        return accountSecurityService.getMinimumBalanceRequirementsByAcctID(acctID);
    }
    protected BigDecimal getAdjustedAmount(final BigDecimal currentBalance, final BigDecimal newBalance){
        return calculationEngine.getAdjustedAmount(currentBalance, newBalance);
    }

    protected void assertListNotEmpty(List<S> transactionSummaries){
        if(transactionSummaries == null || transactionSummaries.isEmpty()){
            throw new NonEmptyListRequiredException("Balance Summary cannot be empty");
        }
    }

    protected void assertBalanceSummaryNotNull(S balanceSummary){
        if(balanceSummary == null){
            throw new InvalidDepositException("Invalid Deposit Balance Summary.");
        }
    }


    protected void assertTransactionListNotNull(List<T> transactions){
        if(transactions == null || transactions.isEmpty()){
            throw new NonEmptyListRequiredException("Transactions Found Empty or Null");
        }
    }

    protected void assertAccountBalancesMapNotNull(Map<Integer, BigDecimal> accountBalances){
        if(accountBalances.isEmpty()){
            throw new IllegalArgumentException("Found Empty Account Balances");
        }
    }

    protected BigDecimal getMinimumBalanceRequirementsByAcctID(final int acctID){
        LOGGER.info("Getting Minimum Balance for AcctID " + acctID);
        BigDecimal minimumBalance = getMinimumBalanceRequirements(acctID);
        LOGGER.info("Minimum Balance for AcctID " + acctID + ": $" + minimumBalance);
        if(minimumBalance == null){
            throw new RuntimeException("Minimum Balance not found.");
        }
        return accountSecurityService.getMinimumBalanceRequirementsByAcctID(acctID);
    }

    protected void addBalanceHistoriesToDatabase(final List<BalanceHistoryEntity> balanceHistoryEntities){
        if(balanceHistoryEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to add balance histories to the database due to empty list.");
        }
        for(BalanceHistoryEntity balanceHistoryEntity : balanceHistoryEntities){
            saveBalanceHistory(balanceHistoryEntity);
        }
    }

    private void saveBalanceHistory(final BalanceHistoryEntity balanceHistory){
        if(balanceHistory == null){
            throw new IllegalArgumentException("Saving Invalid Balance History.");
        }
        LOGGER.info("Saving BalanceHistory: " + balanceHistory.toString());
        balanceHistoryService.save(balanceHistory);
    }

    protected BigDecimal getCurrentBalanceByAcctID(final int acctID){
        return accountService.getBalanceByAcctID(acctID);
    }

    abstract protected List<T> retrieveTransactionsByUserID(int userID);

    abstract protected void sendNotification(List<S> balanceSummaries);

    protected int bulkUpdateAccountBalances(final Map<Integer, BigDecimal> accountBalanceMap){
        if(!accountBalanceMap.isEmpty()){
            for(Map.Entry<Integer, BigDecimal> accountBalances : accountBalanceMap.entrySet()) {
                int acctID = accountBalances.getKey();
                BigDecimal balance = accountBalances.getValue();
                try {
                    LOGGER.info("Updating AcctID: " + acctID);
                    LOGGER.info("Updating Balance: $" + balance);
                    updateBalanceForAccountID(balance, acctID);
                    successCount++;

                } catch (Exception e) {
                    LOGGER.error("Failed to update balance for AcctID: " + acctID + ", Error: " + e.getMessage());
                }
            }
        }else{
            throw new IllegalArgumentException("Unable to retrieve Account Balances for updating...");
        }
        if (successCount == accountBalanceMap.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    abstract protected Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(List<T> transactionList);

    protected Map<Integer, BigDecimal> retrieveCurrentAccountBalancesByAcctID(Set<Integer> acctIDs){
        Map<Integer, BigDecimal> accountBalanceMapByAcctID = new HashMap<>();
        if(!acctIDs.isEmpty()){
            if(accountService == null){
                LOGGER.error("Null Account Service");
            }
            for(Integer acctID : acctIDs){

                // If the accountID is less than zero or (invalid)
                if(acctID <= 0){
                    throw new IllegalArgumentException("Illegal AccountID Found.");
                }
                LOGGER.info("Found AcctID: " + acctID);

                // Get the Account Balance
                BigDecimal balance = accountService.getBalanceByAcctID(acctID);
                if(balance == null){
                    throw new NullPointerException("Null Balance Found.");
                }
                if(balance.equals(BigDecimal.ZERO)){
                    throw new ZeroBalanceException("AccountID: " + acctID + " found with zero balance.");
                }

                // Is the balance greater than the minimum balance requirement?
                BigDecimal minimumBalanceRequirement = getMinimumBalanceRequirementsByAcctID(acctID);
                if(minimumBalanceRequirement == null){
                    throw new RuntimeException("No Minimum balance found for this account.");
                }
                boolean isLessThanMinimumBalance = balance.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(minimumBalanceRequirement) < 0;
                if(isLessThanMinimumBalance){
                    throw new RuntimeException("AccountID: " + acctID + " does not meet the minimum balance requirements of $" + minimumBalanceRequirement);
                }
                accountBalanceMapByAcctID.put(acctID, balance);
            }
            return accountBalanceMapByAcctID;
        }
        else{
            throw new IllegalArgumentException("Cannot Retrieve balances for an empty set of accountIDs.");
        }
    }

    @Deprecated
    protected BigDecimal getCalculation(final BigDecimal amount, final BigDecimal balance, final CalculationStrategy calculationStrategy){
        if(amount == null || balance == null){
            throw new IllegalArgumentException("Unable to calculate deposit from null amount or balance");
        }
        if(amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(BigDecimal.ZERO) > 0){
            return calculationStrategy.calculate(amount, balance);
        }
        else if(balance.equals(BigDecimal.ZERO)){
            throw new ZeroBalanceException("Invalid Balance Found.");
        }
        else if(amount.equals(BigDecimal.ZERO)){
            return balance;
        }
        return BigDecimal.ZERO;
    }


    abstract protected Set<Integer> retrieveAccountIDSet(List<T> transactionList);

    abstract protected Map<Integer, BigDecimal> retrieveTransactionAmountByAcctID(List<T> transactionList);

    abstract protected Map<Integer, List<S>> generateBalanceSummaryMap(List<T> transactions, Map<Integer, BigDecimal> accountBalances);

    abstract protected BalanceHistoryEntity createBalanceHistoryEntity(S balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount);

    abstract protected List<BalanceHistoryEntity> convertBalanceSummaryToBalanceHistoryEntityList(List<S> transactionSummaries);
}
