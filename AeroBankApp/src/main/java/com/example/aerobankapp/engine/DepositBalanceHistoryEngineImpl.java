package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.BalanceHistoryDTO;
import com.example.aerobankapp.workbench.transactions.Deposit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Queue;

public class DepositBalanceHistoryEngineImpl implements BalanceHistoryEngine<Deposit>
{

    @Override
    public Queue<BalanceHistoryDTO> getBalanceHistoryBatch(Queue<Deposit> transactionList)
    {
        return null;
    }

    @Override
    public BalanceHistoryDTO getBalanceHistory(String acctID) {
        return null;
    }

    @Override
    public BigDecimal getAdjustedBalance(String acctID, BigDecimal amount) {
        return null;
    }

    @Override
    public BigDecimal getLatestBalance(AccountDTO account) {
        return null;
    }

    @Override
    public void recordBalanceHistory(AccountDTO account, BigDecimal currentBalance, BigDecimal adjusted, BigDecimal lastBalance) {

    }

    @Override
    public Queue<BalanceHistoryDTO> getBalanceHistoryBatchByDate(String acctID, LocalDate date) {
        return null;
    }

    @Override
    public BalanceHistoryDTO getBalanceHistoryByDate(String acctID, LocalDate date) {
        return null;
    }

    @Override
    public Queue<BalanceHistoryDTO> getBalanceHistoryBatchByDateRange(String acctID, LocalDate startDate, LocalDate endDate) {
        return null;
    }
}
