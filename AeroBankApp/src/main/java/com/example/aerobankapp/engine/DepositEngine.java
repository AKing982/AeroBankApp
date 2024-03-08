package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.dto.MessageNotificationDTO;
import com.example.aerobankapp.dto.ProcessedDepositDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.NotificationEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.scheduler.SchedulerEngine;
import com.example.aerobankapp.scheduler.SchedulerEngineImpl;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.DepositService;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.TransactionType;
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

@Service
@Getter
@Setter
@PreAuthorize("isAuthenticated()")
public class DepositEngine {
    private final DepositQueue depositQueue;
    private List<Deposit> deposits;
    private List<BigDecimal> processedBalances;
    private final List<ProcessedDepositDTO> processedDeposits;
    private final CalculationEngine calculationEngine;
    private final Map<String, BigDecimal> accountCodeToBalanceMap;
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final DepositService depositService;
    private final Logger LOGGER = LoggerFactory.getLogger(DepositEngine.class);

    @Autowired
    public DepositEngine(DepositQueue depositQueue,
                         CalculationEngine calculationEngine,
                         AccountService accountService,
                         DepositService depositService,
                         NotificationService notificationService) {
        this.depositQueue = depositQueue;
        this.calculationEngine = calculationEngine;
        this.accountService = accountService;
        this.depositService = depositService;
        this.notificationService = notificationService;
        this.processedDeposits = new ArrayList<>();
        this.processedBalances = new ArrayList<>();
        this.accountCodeToBalanceMap = new HashMap<>();
    }

    public void setDepositQueue(Deposit deposit) {
        this.depositQueue.add(deposit);
    }

    public List<Deposit> getDepositsFromQueue()
    {
        return depositQueue.getAllElements();
    }

    public void updateAccountBalances(final Map<Integer, BigDecimal> processedDepositMap) {
        processedDepositMap.forEach((accountID, balance) -> {
            if (balance != null) {
                accountService.updateAccountBalanceByAcctID(balance, accountID);
            }
        });
    }

    public Map<Integer, BigDecimal> getProcessedBalances(final List<ProcessedDepositDTO> processedDeposits) {
        Map<Integer, BigDecimal> accountBalances = new HashMap<>();
        for (ProcessedDepositDTO depositDTO : processedDeposits) {
            if (depositDTO.newBalance() == null || depositDTO.accountID() == 0) {
                throw new IllegalArgumentException("Invalid AccountID or Balance in deposit: " + depositDTO);
            }
            accountBalances.put(depositDTO.accountID(), depositDTO.newBalance());
        }
        return accountBalances;
    }

    private void updateProcessedDepositList(final List<ProcessedDepositDTO> deposits) {
        processedDeposits.addAll(deposits);
    }

    public List<ProcessedDepositDTO> process() {
        List<Deposit> unProcessedDeposits = getDepositsFromQueue();
        return getProcessedDeposits(unProcessedDeposits);
    }

    public void processAndUpdateDeposits()
    {
        // Step 1: Process the deposits
        List<Deposit> deposits = getDepositsFromQueue();

        List<ProcessedDepositDTO> processedDepositDTOS = getProcessedDeposits(deposits);

        // Step 2: Extract the processed balances
        Map<Integer, BigDecimal> processedDepositMap = getProcessedBalances(processedDepositDTOS);

        // Step 3: Update the account balances
        updateAccountBalances(processedDepositMap);

        // Step 4: Update the Processed Deposit List
        updateProcessedDepositList(processedDepositDTOS);

        // Step 5: Send notification to user
        for(ProcessedDepositDTO processedDepositDTO : processedDeposits)
        {
            sendNotification(processedDepositDTO);
        }
    }


    public List<ProcessedDepositDTO> getProcessedDeposits(final List<Deposit> depositList)
    {
        List<ProcessedDepositDTO> processedDeposits = new ArrayList<>();
        if(depositList != null) {
            for (Deposit depositDTO : depositList) {
                ProcessedDepositDTO processedDeposit = processIndividualDeposit(depositDTO);
                processedDeposits.add(processedDeposit);
            }
        } else {
            throw new NullPointerException("Deposits list contains nulls, terminating processing...");
        }
        return processedDeposits;
    }

    public ProcessedDepositDTO processIndividualDeposit(final Deposit deposit) {
        if(deposit != null)
        {
            final String accountCode = deposit.getAcctCode();
            final int userID = deposit.getUserID();
            final BigDecimal amount = deposit.getAmount();
            if(accountCode == null || userID < 1 || amount == null)
            {
                throw new IllegalArgumentException("Invalid Deposit Criteria.");
            }

            // Build the DepositsEntity
            DepositsEntity depositsEntity = createDepositEntity(deposit);

            // Save the Deposit to the database.
            depositService.save(depositsEntity);

            BigDecimal balance = getCurrentBalance(accountCode, userID);
            BigDecimal newBalance = getNewBalance(amount, balance);
            addBalanceToList(newBalance);
            return buildProcessedDepositDTO(deposit, newBalance);
        }
        throw new IllegalArgumentException("Processing Invalid Deposit!");
    }

    private DepositsEntity createDepositEntity(final Deposit depositDTO)
    {
        return DepositsEntity.builder()
                .scheduleInterval(depositDTO.getScheduleInterval())
                .amount(depositDTO.getAmount())
                .depositID(depositDTO.getDepositID())
                .posted(LocalDate.now())
                .scheduledDate(depositDTO.getDateScheduled())
                .scheduledTime(depositDTO.getTimeScheduled())
                .description(depositDTO.getDescription())
                .user(UserEntity.builder().userID(depositDTO.getUserID()).build())
                .account(AccountEntity.builder().accountCode(depositDTO.getAcctCode()).acctID(depositDTO.getAccountID()).build())
                .build();
    }

    private void addBalanceToList(BigDecimal balance)
    {
        processedBalances.add(balance);
    }

    private BigDecimal getCurrentBalance(final String acctCode, final int userID)
    {
        return accountService.getBalanceByAccountCodeUserID(acctCode, userID);
    }

    private BigDecimal getNewBalance(final BigDecimal amount, final BigDecimal balance)
    {
        return calculationEngine.calculateDeposit(amount, balance);
    }

    private ProcessedDepositDTO buildProcessedDepositDTO(final Deposit deposit, final BigDecimal balance)
    {
        return ProcessedDepositDTO.builder()
                .accountID(deposit.getAccountID())
                .depositID(deposit.getDepositID())
                .description(deposit.getDescription())
                .newBalance(balance)
                .accountCode(deposit.getAcctCode())
                .userID(deposit.getUserID())
                .amount(deposit.getAmount())
                .createdAt(LocalDateTime.now())
                .build();
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

    private NotificationEntity createNotification(ProcessedDepositDTO depositDTO)
    {

        String message = "You're deposit of $" + depositDTO.amount() + " has been processed Successfully.";

        return NotificationEntity.builder()
                .message(message)
                .userEntity(UserEntity.builder().userID(depositDTO.userID()).build())
                .sent(LocalDateTime.now())
                .hasBeenRead(false)
                .priority(1)
                .build();
    }

    private void sendNotificationToSystem(ProcessedDepositDTO depositDTO)
    {
        NotificationEntity notification1 = createNotification(depositDTO);
        notificationService.createNotification(notification1);
    }

    private void sendNotification(ProcessedDepositDTO deposit)
    {
        // Save the Deposit Notification to the database
        sendNotificationToSystem(deposit);

        final String message = "You're deposit of $" + deposit.amount() + " has been processed Successfully.";

        // Build the Message notification
        MessageNotificationDTO messageNotificationDTO = MessageNotificationDTO.builder()
                        .message(message)
                        .sender(null)
                        .receiver(null)
                        .title("Deposit Notification")
                        .sent(LocalDateTime.now())
                        .build();

        // Send the notification to the user
        notificationService.sendMessageNotification(messageNotificationDTO);
    }


}
