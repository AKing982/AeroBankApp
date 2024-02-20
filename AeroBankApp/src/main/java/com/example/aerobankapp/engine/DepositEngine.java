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
import lombok.Setter;
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
@Setter
public class DepositEngine extends Engine<DepositDTO>
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

    public void setDepositQueue(DepositDTO depositDTO)
    {
        this.depositQueue.add(depositDTO);
    }

    public List<DepositDTO> getDepositsFromQueue() {
        return depositQueue.getAllElements();
    }

    public void updateAccountBalances(final Map<Integer, BigDecimal> processedDepositMap)
    {
        processedDepositMap.forEach((accountID, balance) -> {
            if(balance != null)
            {
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


    public List<ProcessedDepositDTO> processDeposits() {
        List<DepositDTO> unProcessedDeposits = getDepositsFromQueue();
        List<ProcessedDepositDTO> processedDepositDTOS = new ArrayList<>();
        for(DepositDTO depositDTO : unProcessedDeposits)
        {
            try
            {
                processIndividualDeposit(depositDTO);

            }catch(Exception e)
            {
                LOGGER.error("Error processing deposit for account {}: {}", depositDTO.getAccountID(), e.getMessage());
            }
        }

        // Retrieve the Account Balances from the processed deposits
        Map<Integer, BigDecimal> accountBalanceMap = getProcessedBalances(processedDepositDTOS);

        // Update the account Balances
        updateAccountBalances(accountBalanceMap);

        return processedDepositDTOS;
    }

    public ProcessedDepositDTO processIndividualDeposit(final DepositDTO depositDTO) {
        final String accountCode = depositDTO.getAccountCode();
        final int userID = depositDTO.getUserID();
        final BigDecimal amount = depositDTO.getAmount();

        BigDecimal balance = getCurrentBalance(accountCode, userID);
        BigDecimal newBalance = getNewBalance(amount, balance);
        addBalanceToList(newBalance);
        return buildProcessedDepositDTO(depositDTO, newBalance);
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
        final String message = "You're deposit of $" + deposit.getAmount() + " has been processed Successfully.";

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


}
