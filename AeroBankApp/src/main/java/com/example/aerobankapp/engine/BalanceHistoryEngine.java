package com.example.aerobankapp.engine;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.BalanceHistoryDTO;
import com.example.aerobankapp.model.Account;

import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.TransactionDetail;
import com.example.aerobankapp.services.TransactionDetailService;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public interface BalanceHistoryEngine
{

    BalanceHistory getBalanceHistory(int acctID);
    BigDecimal getBalanceBefore(int acctID);
    Map<Long, List<BalanceHistory>> getBalanceHistoriesByAcctIDs(List<Integer> acctIDs);
    List<BalanceHistory> getBalanceHistoriesByAcctID(int acctID);
    void recordBalanceHistory(Account account, BigDecimal currentBalance, BigDecimal adjusted, BigDecimal lastBalance);
    List<BalanceHistory> getBalanceHistoryBatchByDate(int acctID, LocalDate date);
    BalanceHistory getBalanceHistoryByDate(int acctID, LocalDate date);
    List<BalanceHistory> getBalanceHistoryBatchByDateRange(int acctID, LocalDate startDate, LocalDate endDate);
}
