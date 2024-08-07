package com.example.aerobankapp.model;

import com.example.aerobankapp.dto.BalanceHistoryDTO;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BalanceHistoryManager
{
    BalanceHistoryDTO getBalanceHistory(int bHistoryID);

    List<BalanceHistoryDTO> getRecentBalanceHistory(int userID);

    List<BalanceHistoryDTO> getBalanceHistoryForDateRange(String acctID, LocalDate startDate, LocalDate endDate);

    BigDecimal getAverageBalance(String acctID, LocalDate startDate, LocalDate endDate);

    BigDecimal getBalanceAsOf(String acctID, LocalDate date);

    void notifyBalanceChange(String acctID);
}
