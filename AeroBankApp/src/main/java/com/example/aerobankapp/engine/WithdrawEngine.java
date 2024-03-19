package com.example.aerobankapp.engine;

import com.example.aerobankapp.calculation.WithdrawCalculationEngine;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.manager.WithdrawManager;
import com.example.aerobankapp.model.WithdrawBalanceSummary;
import com.example.aerobankapp.services.AccountSecurityService;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.BalanceHistoryService;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WithdrawEngine extends TransactionEngine<Withdraw, WithdrawBalanceSummary>
{

    public WithdrawEngine(AccountService accountService, AccountSecurityService accountSecurityService, NotificationService notificationService, CalculationEngine calculationEngine, BalanceHistoryService balanceHistoryService) {
        super(accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService);
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
}
