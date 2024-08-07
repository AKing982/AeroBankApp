package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.DepositBalanceSummary;
import com.example.aerobankapp.model.Transaction;
import com.example.aerobankapp.model.TransactionDetail;
import com.example.aerobankapp.workbench.transactions.Deposit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DepositProcessor
{
   List<Deposit> retrieveUserDeposits(int userID);

   void sendDepositNotification(List<DepositBalanceSummary> depositBalanceSummaries);

   int bulkUpdateAccountBalances(Map<Integer, BigDecimal> accountBalanceByAcctIDMap);

   Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(List<Deposit> deposits);

   Map<Integer, BigDecimal> retrieveCurrentAccountBalancesByAcctID(Set<Integer> acctIDs);

   Map<Integer, BigDecimal> retrieveDepositAmountByAcctIDMap(List<Deposit> deposits);

   Map<Integer, List<DepositBalanceSummary>> generateDepositBalanceSummaryMap(List<Deposit> deposits, Map<Integer, BigDecimal> accountBalances);

   BalanceHistoryEntity createBalanceHistoryEntity(DepositBalanceSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount);

   List<BalanceHistoryEntity> convertDepositSummaryToBalanceHistoryEntities(List<DepositBalanceSummary> depositList);


}