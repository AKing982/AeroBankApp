package com.example.aerobankapp.engine;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.dto.BalanceHistoryDTO;
import com.example.aerobankapp.workbench.history.BalanceHistory;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Queue;

public interface BalanceHistoryEngine<T extends TransactionBase, S extends AbstractAccountBase>
{
    Queue<BalanceHistoryDTO> getBalanceHistoryBatch(Queue<T> transactionList);
    BalanceHistoryDTO getBalanceHistory(String acctID);
    BigDecimal getAdjustedBalance(String acctID, BigDecimal amount);
    BigDecimal getLatestBalance(S account);
    void recordBalanceHistory(S account, BigDecimal currentBalance, BigDecimal adjusted, BigDecimal lastBalance);
    Queue<BalanceHistoryDTO> getBalanceHistoryBatchByDate(String acctID, LocalDate date);
    BalanceHistoryDTO getBalanceHistoryByDate(String acctID, LocalDate date);
    Queue<BalanceHistoryDTO> getBalanceHistoryBatchByDateRange(String acctID, LocalDate startDate, LocalDate endDate);
}
