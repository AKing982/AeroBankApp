package com.example.aerobankapp.calculation;

import com.example.aerobankapp.workbench.transactions.Withdraw;

import java.util.Queue;

public interface WithdrawCalculationEngine extends CalculationEngine<Withdraw>
{

    @Override
    Queue<Withdraw> calculateBatch(Queue<Withdraw> transactions);

    @Override
    Withdraw calculateSingle(Withdraw transaction);
}
