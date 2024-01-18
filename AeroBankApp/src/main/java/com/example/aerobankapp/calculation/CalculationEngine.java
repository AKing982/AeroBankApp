package com.example.aerobankapp.calculation;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Queue;

public interface CalculationEngine<T extends TransactionBase>
{
    Queue<T> calculateBatch(Queue<T> transactions);
    T calculateSingle(T transaction);
    BigDecimal getFutureBalance(T transaction);
}
