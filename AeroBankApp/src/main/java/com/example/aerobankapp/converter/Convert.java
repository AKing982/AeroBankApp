package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.BalanceHistory;
import com.example.aerobankapp.model.Balance;

public interface Convert
{
    BalanceHistory getConversion(com.example.aerobankapp.workbench.history.BalanceHistory balanceHistory);
}
