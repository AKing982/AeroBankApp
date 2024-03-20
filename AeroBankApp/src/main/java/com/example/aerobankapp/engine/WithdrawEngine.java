package com.example.aerobankapp.engine;

import com.example.aerobankapp.calculation.WithdrawCalculationEngine;
import com.example.aerobankapp.converter.EntityToModelConverter;
import com.example.aerobankapp.converter.WithdrawConverter;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.manager.WithdrawManager;
import com.example.aerobankapp.model.WithdrawBalanceSummary;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WithdrawEngine extends TransactionEngine<Withdraw, WithdrawBalanceSummary> implements Runnable
{
    private final WithdrawService withdrawService;
    private final EntityToModelConverter<WithdrawEntity, Withdraw> withdrawConverter;
    private final Logger LOGGER = LoggerFactory.getLogger(WithdrawEngine.class);

    @Autowired
    public WithdrawEngine(WithdrawService withdrawService, AccountService accountService, AccountSecurityService accountSecurityService, NotificationService notificationService, CalculationEngine calculationEngine, BalanceHistoryService balanceHistoryService, EncryptionService encryptionService) {
        super(accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
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
    protected List<Withdraw> retrieveTransactionsByUserID(int userID) {
        return null;
    }

    @Override
    protected void sendNotification(List<WithdrawBalanceSummary> balanceSummaries) {

    }

    @Override
    protected int bulkUpdateAccountBalances(Map<Integer, BigDecimal> accountBalanceMap) {
        return 0;
    }

    @Override
    protected Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(List<Withdraw> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveCurrentAccountBalancesByAcctID(Set<Integer> acctIDs) {
        return null;
    }

    @Override
    protected Set<Integer> retrieveAccountIDSet(List<Withdraw> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveTransactionAmountByAcctID(List<Withdraw> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, List<WithdrawBalanceSummary>> generateBalanceSummaryMap(List<Withdraw> transactions, Map<Integer, BigDecimal> accountBalances) {
        return null;
    }

    @Override
    protected BalanceHistoryEntity createBalanceHistoryEntity(WithdrawBalanceSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount) {
        return null;
    }

    @Override
    protected List<BalanceHistoryEntity> convertBalanceSummaryToBalanceHistoryEntityList(List<WithdrawBalanceSummary> transactionSummaries) {
        return null;
    }

    @Override
    public void run() {

    }
}
