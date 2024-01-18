package com.example.aerobankapp.calculation;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.util.List;

public interface CalculationEngine<T extends TransactionBase>
{
    List<T> calculateBatch(List<T> transactions);
    T calculateSingle(T transaction);
}
