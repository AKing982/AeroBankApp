package com.example.aerobankapp.engine;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.BalanceHistoryDTO;
import com.example.aerobankapp.model.Account;

import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Queue;

public interface BalanceHistoryEngine
{

    List<TransactionDetail> getTransactionDetails();
    BalanceHistoryDTO getBalanceHistory(String acctID);
    BigDecimal getAdjustedBalance(String acctID, BigDecimal amount);
    BigDecimal getLatestBalance(Account account);
    void recordBalanceHistory(Account account, BigDecimal currentBalance, BigDecimal adjusted, BigDecimal lastBalance);
    List<BalanceHistoryDTO> getBalanceHistoryBatchByDate(String acctID, LocalDate date);
    BalanceHistoryDTO getBalanceHistoryByDate(String acctID, LocalDate date);
    List<BalanceHistory> getBalanceHistoryBatchByDateRange(String acctID, LocalDate startDate, LocalDate endDate);
}
