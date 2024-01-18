package com.example.aerobankapp.calculation;

import com.example.aerobankapp.workbench.transactions.Withdraw;

import java.util.List;

public interface WithdrawCalculationEngine extends CalculationEngine<Withdraw>
{

    @Override
    List<Withdraw> calculateBatch(List<Withdraw> transactions);

    @Override
    Withdraw calculateSingle(Withdraw transaction);
}
