package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.PurchaseTransferSummary;
import com.example.aerobankapp.model.TransactionBalanceSummary;
import com.example.aerobankapp.services.AccountSecurityService;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.BalanceHistoryService;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PurchaseEngine extends TransactionEngine<Purchase, PurchaseTransferSummary>
{

    public PurchaseEngine(AccountService accountService, AccountSecurityService accountSecurityService, NotificationService notificationService, CalculationEngine calculationEngine, BalanceHistoryService balanceHistoryService) {
        super(accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService);
    }

    @Override
    protected List<Purchase> fetchAll() {
        return null;
    }

    @Override
    protected List<Purchase> retrieveTransactionsByUserID(int userID) {
        return null;
    }

    @Override
    protected void sendNotification(List<PurchaseTransferSummary> balanceSummaries) {

    }

    @Override
    protected int bulkUpdateAccountBalances(Map<Integer, BigDecimal> accountBalanceMap) {
        return 0;
    }

    @Override
    protected Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(List<Purchase> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveCurrentAccountBalancesByAcctID(Set<Integer> acctIDs) {
        return null;
    }

    @Override
    protected BigDecimal getCalculation(BigDecimal amount, BigDecimal balance) {
        return null;
    }

    @Override
    protected Set<Integer> retrieveAccountIDSet(List<Purchase> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveTransactionAmountByAcctID(List<Purchase> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, List<PurchaseTransferSummary>> generateBalanceSummaryMap(List<Purchase> transactions, Map<Integer, BigDecimal> accountBalances) {
        return null;
    }

    @Override
    protected BalanceHistoryEntity createBalanceHistoryEntity(PurchaseTransferSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount) {
        return null;
    }

    @Override
    protected List<BalanceHistoryEntity> convertBalanceSummaryToBalanceHistoryEntityList(List<PurchaseTransferSummary> transactionSummaries) {
        return null;
    }


}
