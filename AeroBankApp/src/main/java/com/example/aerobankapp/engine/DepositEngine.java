package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.dto.MessageNotificationDTO;
import com.example.aerobankapp.dto.ProcessedDepositDTO;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.DepositBalanceSummary;
import com.example.aerobankapp.scheduler.SchedulerEngine;
import com.example.aerobankapp.scheduler.SchedulerEngineImpl;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.TransactionType;
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
        return null;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveTransactionAmountByAcctID(List<Deposit> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, List<DepositBalanceSummary>> generateBalanceSummaryMap(List<Deposit> transactions, Map<Integer, BigDecimal> accountBalances) {
        return null;
    }

    @Override
    protected BalanceHistoryEntity createBalanceHistoryEntity(DepositBalanceSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount) {
        return null;
    }

    @Override
    protected List<BalanceHistoryEntity> convertBalanceSummaryToBalanceHistoryEntityList(List<DepositBalanceSummary> transactionSummaries) {
        return null;
    }

    @Override
    public void run() {

    }
}
