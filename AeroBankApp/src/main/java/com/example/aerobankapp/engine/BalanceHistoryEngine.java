package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.BalanceHistoryDTO;
import com.example.aerobankapp.workbench.history.BalanceHistory;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.util.Queue;

public interface BalanceHistoryEngine<T extends TransactionBase>
{
    Queue<BalanceHistoryDTO> getBalanceHistoryBatch(Queue<T> transactionList);
    BalanceHistoryDTO getBalanceHistory(T transaction);


}
