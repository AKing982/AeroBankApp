package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.TransactionBalanceSummary;
import com.example.aerobankapp.model.TransferBalanceSummary;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TransferEngine extends TransactionEngine<Transfer, TransferBalanceSummary>
{

    @Autowired
    public TransferEngine(AccountService accountService, AccountSecurityService accountSecurityService, NotificationService notificationService, CalculationEngine calculationEngine, BalanceHistoryService balanceHistoryService) {
        super(accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService);
    }

    @Override
    protected List<Transfer> retrieveTransactionsByUserID(int userID) {
        return null;
    }

    @Override
    protected void sendNotification(List<TransferBalanceSummary> balanceSummaries) {

    }

    @Override
    protected int bulkUpdateAccountBalances(Map<Integer, BigDecimal> accountBalanceMap) {
        return 0;
    }

    @Override
    protected Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(List<Transfer> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveCurrentAccountBalancesByAcctID(Set<Integer> acctIDs) {
        return null;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveTransactionAmountByAcctID(List<Transfer> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, List<TransferBalanceSummary>> generateBalanceSummaryMap(List<Transfer> transactions, Map<Integer, BigDecimal> accountBalances) {
        return null;
    }

    @Override
    protected BalanceHistoryEntity createBalanceHistoryEntity(TransferBalanceSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount) {
        return null;
    }

    @Override
    protected List<BalanceHistoryEntity> convertBalanceSummaryToBalanceHistoryEntityList(List<TransferBalanceSummary> transactionSummaries) {
        return null;
    }


}
