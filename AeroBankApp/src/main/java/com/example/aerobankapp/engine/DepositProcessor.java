package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.TransactionDetail;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DepositProcessor
{
   List<Deposit> retrieveUserDeposits(int userID);

   void sendDepositNotification(List<TransactionDetail> transactionDetails);

   void updateAccountBalances(Map<Integer, BigDecimal> accountBalanceByAcctIDMap);

   Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(List<Deposit> deposits);

   Map<Integer, BigDecimal> retrieveCurrentAccountBalancesByAcctID(Set<Integer> acctIDs);

   List<TransactionDetail> convertDepositsToTransactionDetail(List<Deposit> depositList);

   void process();

}
