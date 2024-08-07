package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BalanceHistoryUtil{

    public static BalanceHistory convertBalanceSummaryToBalanceHistoryModel(DepositBalanceSummary deposit, BigDecimal currentBalance, BigDecimal adjustedAmount){
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setCurrentBalance(currentBalance);
        balanceHistory.setNewBalance(deposit.getPostBalance());
        balanceHistory.setAccountID(deposit.getTransaction().getAccountID());
        balanceHistory.setAdjustedAmount(adjustedAmount);
        return balanceHistory;
    }

    public static BalanceHistory convertWithdrawBalanceSummaryToBalanceHistory(WithdrawBalanceSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjusted){
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setCurrentBalance(currentBalance);
        balanceHistory.setNewBalance(balanceSummary.getPostBalance());
        balanceHistory.setAccountID(balanceHistory.getFromAccountID());
        balanceHistory.setAdjustedAmount(adjusted);
        return balanceHistory;
    }

    public static BalanceHistory convertTransferBalanceSummaryToBalanceHistoryModel(TransferBalanceSummary transferSummary, BigDecimal currentBalance, BigDecimal adjusted){
        return BalanceHistory.builder()
                .currentBalance(currentBalance)
                .newBalance(transferSummary.getPostBalance())
                .adjustedAmount(adjusted)
                .toAccountID(transferSummary.getTransaction().getToAccountID())
                .fromAccountID(transferSummary.getTransaction().getFromAccountID())
                .build();
    }

    public static BalanceHistoryEntity convertBalanceHistoryToEntity(BalanceHistory balanceHistory){
        BalanceHistoryEntity balanceHistoryEntity = new BalanceHistoryEntity();
        balanceHistoryEntity.setHistoryID(balanceHistoryEntity.getHistoryID());
        balanceHistoryEntity.setPostBalance(balanceHistory.getNewBalance());
        balanceHistoryEntity.setPreBalance(balanceHistory.getCurrentBalance());
        balanceHistoryEntity.setAccount(AccountEntity.builder().acctID(balanceHistory.getAccountID()).build());
        balanceHistoryEntity.setAdjusted(balanceHistory.getAdjustedAmount());
        balanceHistoryEntity.setPosted(LocalDate.now());
        balanceHistoryEntity.setCreatedAt(LocalDateTime.now());
        balanceHistoryEntity.setCreatedBy("user");
        balanceHistoryEntity.setTransactionType("DEPOSIT");
        return balanceHistoryEntity;
    }
}
