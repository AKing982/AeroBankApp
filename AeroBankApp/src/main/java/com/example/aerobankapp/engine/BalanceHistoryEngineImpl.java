package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.BalanceHistoryDTO;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.TransactionDetail;
import com.example.aerobankapp.services.TransactionDetailService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Getter
public class BalanceHistoryEngineImpl implements BalanceHistoryEngine
{
    private final TransactionDetailService transactionDetailService;

    @Autowired
    public BalanceHistoryEngineImpl(TransactionDetailService transactionDetailService){
        this.transactionDetailService = transactionDetailService;
    }

    @Override
    public List<TransactionDetail> getTransactionDetailsByUserID(int userID) {
        return null;
    }

    @Override
    public BalanceHistory getBalanceHistory(int acctID) {
        return null;
    }

    @Override
    public BigDecimal getCurrentBalance(int acctID) {
        return null;
    }

    @Override
    public Map<Long, List<BalanceHistory>> getBalanceHistoriesByAcctIDs(List<Integer> acctIDs) {
        return null;
    }

    @Override
    public List<BalanceHistory> getBalanceHistoriesByAcctID(int acctID) {
        return null;
    }

    @Override
    public void recordBalanceHistory(Account account, BigDecimal currentBalance, BigDecimal adjusted, BigDecimal lastBalance) {

    }

    @Override
    public List<BalanceHistory> getBalanceHistoryBatchByDate(int acctID, LocalDate date) {
        return null;
    }

    @Override
    public BalanceHistory getBalanceHistoryByDate(int acctID, LocalDate date) {
        return null;
    }

    @Override
    public List<BalanceHistory> getBalanceHistoryBatchByDateRange(int acctID, LocalDate startDate, LocalDate endDate) {
        return null;
    }
}
