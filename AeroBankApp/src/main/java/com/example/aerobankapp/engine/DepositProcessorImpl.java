package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.ZeroBalanceException;
import com.example.aerobankapp.model.TransactionDetail;
import com.example.aerobankapp.services.AccountSecurityService;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.DepositService;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.workbench.transactions.Deposit;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
public class DepositProcessorImpl implements DepositProcessor
{
    private final DepositService depositService;
    private final AccountService accountService;
    private final AccountSecurityService accountSecurityService;
    private final NotificationService notificationService;
    private final CalculationEngine calculationEngine;

    // Need a class that encrypts/decrypts the deposit data

    private final Logger LOGGER = LoggerFactory.getLogger(DepositProcessorImpl.class);

    @Autowired
    public DepositProcessorImpl(DepositService depositService,
                                AccountService accountService,
                                AccountSecurityService accountSecurityService,
                                NotificationService notificationService,
                                CalculationEngine calculationEngine)
    {
        this.depositService = depositService;
        this.accountService = accountService;
        this.accountSecurityService = accountSecurityService;
        this.notificationService = notificationService;
        this.calculationEngine = calculationEngine;
    }

    @Override
    public List<Deposit> retrieveUserDeposits(int userID) {
        if(userID < 1){
            throw new InvalidUserIDException("Invalid UserID Found: " + userID);
        }
        List<DepositsEntity> depositsEntities = depositService.findByUserID(userID);

        if(!depositsEntities.isEmpty()){
            return depositsEntities.stream()
                    .map(this::convertDepositEntityToDeposit)
                    .toList();
        }else{
            throw new RuntimeException("No Deposits found.");
        }
    }

    @Override
    public void sendDepositNotification(List<TransactionDetail> transactionDetails) {

    }

    @Override
    public void updateAccountBalances(Map<Integer, BigDecimal> accountBalanceByAcctIDMap) {

    }

    @Override
    public Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(final List<Deposit> deposits)
    {
        Map<Integer, BigDecimal> accountBalanceMap = new HashMap<>();

        // Get the Set of AccountID's
        Set<Integer> accountIDs = retrieveDepositAccountIDsSet(deposits);

        // Retrieve the current account balances
        Map<Integer, BigDecimal> currentAccountBalances = retrieveCurrentAccountBalancesByAcctID(accountIDs);

        // Calculate the new Account Balances
        for(Map.Entry<Integer, BigDecimal> entry : currentAccountBalances.entrySet()){

        }

        return null;
    }

    @Override
    public List<TransactionDetail> convertDepositsToTransactionDetail(List<Deposit> depositList) {
        return null;
    }

    @Override
    public void process() {

    }

    public Set<Integer> retrieveDepositAccountIDsSet(final List<Deposit> deposits){
        Set<Integer> accountIDSet = new HashSet<>();
        for(Deposit deposit : deposits){
            int acctID = deposit.getAccountID();
            accountIDSet.add(acctID);
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

                // Is the balance greater than the minimum balance requirement?
                BigDecimal minimumBalanceRequirement = accountSecurityService.getMinimumBalanceRequirementsByAcctID(acctID);
                boolean isLessThanMinimumBalance = balance.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(minimumBalanceRequirement) < 0;
                if(balance.equals(BigDecimal.ZERO)){
                    throw new ZeroBalanceException("AccountID: " + acctID + " found with zero balance.");
                }
                if(balance == null){
                    throw new NullPointerException("Null Balance Found.");
                }
                if(isLessThanMinimumBalance){
                    throw new RuntimeException("Found Balance is below minimum balance requirements.");
                }

                accountBalanceMapByAcctID.put(acctID, balance);
            }
            return accountBalanceMapByAcctID;
        }
        else{
            throw new IllegalArgumentException("Cannot Retrieve balances for an empty set of accountIDs.");
        }
    }

    public BigDecimal getDepositCalculation(BigDecimal amount, BigDecimal balance)
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


    private Deposit convertDepositEntityToDeposit(final DepositsEntity depositsEntity){
        Deposit deposit = new Deposit();
        deposit.setDepositID(depositsEntity.getDepositID());
        deposit.setScheduleInterval(depositsEntity.getScheduleInterval());
        deposit.setDateScheduled(depositsEntity.getScheduledDate());
        deposit.setDescription(depositsEntity.getDescription());
        deposit.setAcctCode(depositsEntity.getAccount().getAccountCode());
        deposit.setUserID(depositsEntity.getUser().getUserID());
        deposit.setAmount(depositsEntity.getAmount());
        deposit.setTimeScheduled(depositsEntity.getScheduledTime());
        deposit.setAccountID(depositsEntity.getAccount().getAcctID());
        deposit.setDate_posted(LocalDate.now());
        return deposit;
    }
}
