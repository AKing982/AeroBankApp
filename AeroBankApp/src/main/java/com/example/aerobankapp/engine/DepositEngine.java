package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.dto.MessageNotificationDTO;
import com.example.aerobankapp.dto.ProcessedDepositDTO;
import com.example.aerobankapp.entity.NotificationEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.scheduler.SchedulerEngine;
import com.example.aerobankapp.scheduler.SchedulerEngineImpl;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.mchange.v2.collection.MapEntry;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Getter
public class DepositEngine extends Engine<DepositDTO> implements Runnable
{
    private final DepositQueue depositQueue;
    private List<DepositDTO> deposits;
    private List<BigDecimal> processedBalances;
    private final List<ProcessedDepositDTO> processedDeposits;
    private final CalculationEngine calculationEngine;
    private final Map<String, BigDecimal> accountCodeToBalanceMap;
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final Logger LOGGER = LoggerFactory.getLogger(DepositEngine.class);

    @Autowired
    public DepositEngine(DepositQueue depositQueue,
                         CalculationEngine calculationEngine,
                         AccountService accountService,
                         NotificationService notificationService) {
        this.depositQueue = depositQueue;
        this.calculationEngine = calculationEngine;
        this.accountService = accountService;
        this.notificationService = notificationService;
        this.processedDeposits = new ArrayList<>();
        this.processedBalances = new ArrayList<>();
        this.accountCodeToBalanceMap = new HashMap<>();
    }

    public List<DepositDTO> getDepositsFromQueue() {
        return depositQueue.getAllElements();
    }

    public void updateAccountBalances(final Map<Integer, BigDecimal> processedDepositMap)
    {
        for(Map.Entry<Integer, BigDecimal> entry : processedDepositMap.entrySet())
        {
            int accountID = entry.getKey();
            BigDecimal balance = entry.getValue();
            if(balance != null)
            {
                accountService.updateAccountBalanceByAcctID(balance, accountID);
            }
        }
    }

    public Map<Integer, BigDecimal> getProcessedBalances(final List<ProcessedDepositDTO> processedDeposits)
    {
        Map<Integer, BigDecimal> accountBalances = new HashMap<>();
        if (!processedDeposits.isEmpty())
        {
            for (ProcessedDepositDTO depositDTO : processedDeposits)
            {
                if(depositDTO.newBalance() != null || depositDTO.accountID() != 0)
                {
                    accountBalances.put(depositDTO.accountID(), depositDTO.newBalance());
                }
                else
                {
                    throw new IllegalArgumentException("Invalid AccountID and Balance found, unable to process deposits at this time.");
                }
            }
        }
        return accountBalances;
    }

    private void updateProcessedDepositList(final List<ProcessedDepositDTO> deposits) {
        processedDeposits.addAll(deposits);
    }


    public List<ProcessedDepositDTO> processDeposits() {
        List<DepositDTO> unProcessedDeposits = getDepositsFromQueue();
        List<ProcessedDepositDTO> processedDepositDTOS = new ArrayList<>();
        for (DepositDTO depositDTO : unProcessedDeposits) {
            // Validate the deposit
            validateDeposit(depositDTO);

            // Process the deposit
            ProcessedDepositDTO processedDeposit = processDeposit(depositDTO);

            // Add the deposit to the processed list
            processedDepositDTOS.add(processedDeposit);

            // Notify the Account Holder
            notifyAccountHolder(depositDTO);
        }

        // Update the Processed DepositList
        updateProcessedDepositList(processedDepositDTOS);

        // Retrieve the Account Balances from the processed deposits
        Map<Integer, BigDecimal> accountBalanceMap = getProcessedBalances(processedDepositDTOS);

        // Update the account Balances
        updateAccountBalances(accountBalanceMap);

        return processedDepositDTOS;
    }

    private ProcessedDepositDTO processDeposit(final DepositDTO depositDTO) {
        String accountCode = depositDTO.getAccountCode();
        int userID = depositDTO.getUserID();
        BigDecimal amount = depositDTO.getAmount();

        BigDecimal balance = accountService.getBalanceByAccountCodeUserID(accountCode, userID);
        BigDecimal newBalance = calculationEngine.calculateDeposit(amount, balance);
        processedBalances.add(newBalance);
        return buildProcessedDepositDTO(depositDTO, newBalance);
    }

    private ProcessedDepositDTO buildProcessedDepositDTO(final DepositDTO depositDTO, final BigDecimal balance)
    {
        return ProcessedDepositDTO.builder()
                .accountID(depositDTO.getAccountID())
                .depositID(depositDTO.getDepositID())
                .description(depositDTO.getDescription())
                .newBalance(balance)
                .accountCode(depositDTO.getAccountCode())
                .userID(depositDTO.getUserID())
                .amount(depositDTO.getAmount())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void validateDeposit(DepositDTO depositDTO)
    {
        Objects.requireNonNull(depositDTO.getAccountCode(), "Non Null AccountCode");
        Objects.requireNonNull(depositDTO.getAmount(), "Non Null Amount Required");
    }

    @Override
    protected BigDecimal calculateTransactionFee()
    {
        return null;
    }

    @Override
    protected BigDecimal convertCurrency(BigDecimal amount, Currency fromCurrency, Currency toCurrency)
    {
        return null;
    }

    @Override
    protected BigDecimal calculateInterest(BigDecimal amount, BigDecimal annualInterestRate)
    {
        return null;
    }

    @Override
    protected BigDecimal calculateTax(BigDecimal amount) {
        return null;
    }


    private TransactionSummary generateTransactionSummary(ProcessedDepositDTO processedDepositDTO)
    {
        return TransactionSummary.builder()
                .transactionID(processedDepositDTO.depositID())
                .accountCode(processedDepositDTO.accountCode())
                .balanceAfterTransaction(processedDepositDTO.newBalance())
                .dateCreated(LocalDate.now())
                .transactionType(TransactionType.DEPOSIT)
                .accountID(processedDepositDTO.accountID())
                .transactionAmount(processedDepositDTO.amount())
                .description(processedDepositDTO.description())
                .build();
    }

    @Override
    protected void storeTransaction(DepositDTO transaction)
    {

    }

    @Override
    protected void createAuditTrail(DepositDTO transaction)
    {

    }

    @Override
    protected void notifyAccountHolder(DepositDTO transaction) {
        sendNotification(transaction);
    }

    private NotificationEntity createNotification(DepositDTO depositDTO)
    {

        String message = "You're deposit of $" + depositDTO.getAmount() + " has been processed Successfully.";

        return NotificationEntity.builder()
                .message(message)
                .userEntity(UserEntity.builder().userID(depositDTO.getUserID()).build())
                .sent(LocalDateTime.now())
                .hasBeenRead(false)
                .priority(1)
                .build();
    }

    private void sendNotificationToSystem(DepositDTO depositDTO)
    {
        NotificationEntity notification1 = createNotification(depositDTO);
        notificationService.createNotification(notification1);
    }

    private void sendNotification(DepositDTO deposit)
    {
        String message = "You're deposit of $" + deposit.getAmount() + " has been processed Successfully.";

        sendNotificationToSystem(deposit);

        MessageNotificationDTO messageNotificationDTO = MessageNotificationDTO.builder()
                        .message(message)
                        .sender(null)
                        .receiver(null)
                        .title("Deposit Notification")
                        .sent(LocalDateTime.now())
                        .build();

        notificationService.sendMessageNotification(messageNotificationDTO);
    }

    @Override
    public void run() {

    }
}
