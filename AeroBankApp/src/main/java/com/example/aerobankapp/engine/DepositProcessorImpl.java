package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.DepositBalanceSummary;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.utilities.BalanceHistoryUtil;
import com.example.aerobankapp.workbench.utilities.DepositProcessorUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static com.example.aerobankapp.workbench.utilities.DepositProcessorUtil.buildDepositBalanceSummary;

@Service
@Getter
public class DepositProcessorImpl implements DepositProcessor, Runnable
{
    private final DepositService depositService;
    private final AccountService accountService;
    private final AccountSecurityService accountSecurityService;
    private final NotificationService notificationService;
    private final CalculationEngine calculationEngine;
    private final UserLogService userLogService;
    private final BalanceHistoryService balanceHistoryService;

    // Need a class that encrypts/decrypts the deposit data
    private final EncryptionService encryptionService;

    private final Logger LOGGER = LoggerFactory.getLogger(DepositProcessorImpl.class);

    @Autowired
    public DepositProcessorImpl(DepositService depositService,
                                AccountService accountService,
                                AccountSecurityService accountSecurityService,
                                NotificationService notificationService,
                                CalculationEngine calculationEngine,
                                UserLogService userLogService,
                                BalanceHistoryService balanceHistoryService,
                                EncryptionService encryptionService)
    {
        this.depositService = depositService;
        this.accountService = accountService;
        this.accountSecurityService = accountSecurityService;
        this.notificationService = notificationService;
        this.calculationEngine = calculationEngine;
        this.userLogService = userLogService;
        this.balanceHistoryService = balanceHistoryService;
        this.encryptionService = encryptionService;
    }


    public List<Deposit> fetchAllDeposits(){
        List<DepositsEntity> allDeposits = depositService.findAll();
        return allDeposits.stream()
                .map(DepositProcessorUtil::convertDepositEntityToDeposit)
                .toList();
    }

    @Override
    public List<Deposit> retrieveUserDeposits(int userID) {
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

    public void updateBalanceForAccountID(BigDecimal balance, int acctID){
        if(acctID > 0 && balance.compareTo(BigDecimal.ZERO) > 0){
            accountService.updateAccountBalanceByAcctID(balance, acctID);
        }
    }

    @Override
    public void sendDepositNotification(List<DepositBalanceSummary> transactionDetails) {
        if(transactionDetails.isEmpty()){
            throw new NonEmptyListRequiredException("Error sending deposit notification due to empty list.");
        }
    }

    @Override
    public int bulkUpdateAccountBalances(final Map<Integer, BigDecimal> accountBalanceByAcctIDMap) {

        int successCount = 0;
        if(!accountBalanceByAcctIDMap.isEmpty()){
            for(Map.Entry<Integer, BigDecimal> accountBalances : accountBalanceByAcctIDMap.entrySet()) {
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
        if (successCount == accountBalanceByAcctIDMap.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(final List<Deposit> deposits)
    {
        Map<Integer, BigDecimal> accountBalanceMap = new HashMap<>();

        // Get the Set of AccountID's
        Set<Integer> accountIDs = retrieveDepositAccountIDsSet(deposits);

        // Retrieve the current account balances
        Map<Integer, BigDecimal> currentAccountBalances = retrieveCurrentAccountBalancesByAcctID(accountIDs);

        // Retrieve the AccountID, Deposit Amount map
        Map<Integer, BigDecimal> depositAmountByAcctIDMap = retrieveDepositAmountByAcctIDMap(deposits);

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


    private BigDecimal getCurrentBalanceByAcctID(int acctID){
        return getAccountService().getBalanceByAcctID(acctID);
    }

    private void saveBalanceHistory(BalanceHistoryEntity balanceHistory){
        if(balanceHistory == null){
            throw new IllegalArgumentException("Saving Invalid Balance History.");
        }
        LOGGER.info("Saving BalanceHistory: " + balanceHistory.toString());
        getBalanceHistoryService().save(balanceHistory);
    }

    private BigDecimal getMinimumBalanceRequirements(final int acctID){
        return getAccountSecurityService().getMinimumBalanceRequirementsByAcctID(acctID);
    }

    private BigDecimal getAdjustedAmount(BigDecimal currentBalance, BigDecimal newBalance){
        return getCalculationEngine().getAdjustedAmount(currentBalance, newBalance);
    }

    public BigDecimal getMinimumBalanceRequirementsByAcctID(final int acctID){
        LOGGER.info("Getting Minimum Balance for AcctID " + acctID);
        BigDecimal minimumBalance = getMinimumBalanceRequirements(acctID);
        LOGGER.info("Minimum Balance for AcctID " + acctID + ": $" + minimumBalance);
        if(minimumBalance == null){
            throw new RuntimeException("Minimum Balance not found.");
        }
        return accountSecurityService.getMinimumBalanceRequirementsByAcctID(acctID);
    }

    @Override
    public Map<Integer, BigDecimal> retrieveDepositAmountByAcctIDMap(final List<Deposit> deposits){
        Map<Integer, BigDecimal> depositAmountByAcctIDMap = new HashMap<>();
        if(!deposits.isEmpty()){
            for(Deposit deposit : deposits){
                final int acctID = deposit.getAccountID();
                final BigDecimal amount = deposit.getAmount();
                if(acctID > 0 && amount.compareTo(BigDecimal.ZERO) > 0){
                    depositAmountByAcctIDMap.put(acctID, amount);
                }else{
                    throw new IllegalArgumentException("Invalid AcctID or Deposit Amount Found.");
                }
            }
            return depositAmountByAcctIDMap;
        }else{
            throw new IllegalArgumentException("No Deposits found.");
        }
    }

    @Override
    public Map<Integer, List<DepositBalanceSummary>> generateDepositBalanceSummaryMap(final List<Deposit> deposits, final Map<Integer, BigDecimal> accountBalances){
        Map<Integer, List<DepositBalanceSummary>> depositBalanceSummaries = new HashMap<>();
        if(deposits.isEmpty() || accountBalances.isEmpty()){
            throw new NonEmptyListRequiredException("Deposits and account balances cannot be empty.");
        }
        for(Deposit deposit : deposits){
            if(deposit != null){
                int acctID = deposit.getAccountID();
                BigDecimal newBalance = accountBalances.get(acctID);
                if(newBalance == null){
                    throw new NullPointerException("Unable to retrieve new balance for accountID: " +  acctID);
                }
                DepositBalanceSummary balanceSummary = buildDepositBalanceSummary(deposit, newBalance);
                depositBalanceSummaries.computeIfAbsent(acctID, k -> new ArrayList<>()).add(balanceSummary);
            }
        }
        return depositBalanceSummaries;
    }


    @Override
    public BalanceHistoryEntity createBalanceHistoryEntity(DepositBalanceSummary depositBalanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount){
      if(depositBalanceSummary == null){
          throw new InvalidDepositException("Invalid Deposit Balance Summary.");
      }
      if(currentBalance == null || adjustedAmount == null){
          throw new IllegalArgumentException("Illegal Balance or Amount found.");
      }

      BalanceHistory balanceHistory = BalanceHistoryUtil.convertBalanceSummaryToBalanceHistoryModel(depositBalanceSummary, currentBalance, adjustedAmount);
      return BalanceHistoryUtil.convertBalanceHistoryToEntity(balanceHistory);
    }


    //TODO: This method will need to incorporate creating the balance history and building a transactions object to store in the transactions table
    @Override
    public List<BalanceHistoryEntity> convertDepositSummaryToBalanceHistoryEntities(List<DepositBalanceSummary> depositSummaries){
        List<BalanceHistoryEntity> balanceHistoryEntities = new ArrayList<>();
        if(depositSummaries.isEmpty()){
            throw new NonEmptyListRequiredException("DepositBalance Summary list cannot be empty.");
        }
        for(DepositBalanceSummary depositBalanceSummary : depositSummaries){
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
            balanceHistoryEntities.add(balanceHistoryEntity);

            if(balanceAfterDeposit == null){
                throw new InvalidBalanceException("Retrieving Invalid Balance from Balance Summary...");
            }
        }
        return balanceHistoryEntities;
    }

    public void addBalanceHistoriesToDatabase(List<BalanceHistoryEntity> balanceHistoryEntities){
        if(balanceHistoryEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to add balance histories to the database due to empty list.");
        }
        for(BalanceHistoryEntity balanceHistoryEntity : balanceHistoryEntities){
            saveBalanceHistory(balanceHistoryEntity);
        }
    }

    public Set<Integer> retrieveDepositAccountIDsSet(final List<Deposit> deposits){
        Set<Integer> accountIDSet = new HashSet<>();
        for(Deposit deposit : deposits){
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
    public Map<Integer, BigDecimal> retrieveCurrentAccountBalancesByAcctID(final Set<Integer> acctIDs)
    {
        Map<Integer, BigDecimal> accountBalanceMapByAcctID = new HashMap<>();
        if(!acctIDs.isEmpty()){
            if(getAccountService() == null){
                LOGGER.error("Null Account Service");
            }
            for(Integer acctID : acctIDs){

                // If the accountID is less than zero or (invalid)
                if(acctID <= 0){
                    throw new IllegalArgumentException("Illegal AccountID Found.");
                }
                LOGGER.info("Found AcctID: " + acctID);

                // Get the Account Balance
                BigDecimal balance = getAccountService().getBalanceByAcctID(acctID);
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
                    throw new RuntimeException("AccountID: " + acctID + " does not meet the minimum balance requirements of: " + minimumBalanceRequirement);
                }
                accountBalanceMapByAcctID.put(acctID, balance);
            }
            return accountBalanceMapByAcctID;
        }
        else{
            throw new IllegalArgumentException("Cannot Retrieve balances for an empty set of accountIDs.");
        }
    }


    public BigDecimal getDepositCalculation(final BigDecimal amount, final BigDecimal balance)
    {
        if(amount == null || balance == null){
            throw new IllegalArgumentException("Unable to calculate deposit from null amount or balance");
        }
        if(amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(BigDecimal.ZERO) > 0){
            return calculationEngine.calculateDeposit(amount, balance);
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
    public void run()
    {
        try {
            // Step 1: Fetch all deposits
            List<Deposit> allDeposits = fetchAllDeposits();
            if(allDeposits.isEmpty()){
                LOGGER.info("No deposits to process.");
                return;
            }
            // Step 2: Get calculated account balances based on the deposits
            Map<Integer, BigDecimal> calculatedAccountBalances = getCalculatedAccountBalanceMap(allDeposits);

            // Step 3: Generate deposit balance summaries for notification or further processing
            Map<Integer, List<DepositBalanceSummary>> depositBalanceSummaryMap = generateDepositBalanceSummaryMap(allDeposits, calculatedAccountBalances);

            // Step 4: Convert deposit summaries to balance history entities
            List<BalanceHistoryEntity> balanceHistoryEntities = new ArrayList<>();
            depositBalanceSummaryMap.forEach((acctID, depositBalanceSummaries) ->
                    balanceHistoryEntities.addAll(convertDepositSummaryToBalanceHistoryEntities(depositBalanceSummaries)));

            // Step 5: Add balance histories to the database
            addBalanceHistoriesToDatabase(balanceHistoryEntities);

            // Step 6: Optionally, send notifications about the deposits
            // This step depends on your application's requirements.
            depositBalanceSummaryMap.values().forEach(this::sendDepositNotification);

            LOGGER.info("Deposit processing completed successfully.");
        } catch(Exception e) {
            LOGGER.error("Error occurred during deposit processing: " + e.getMessage(), e);
        }
    }
}
