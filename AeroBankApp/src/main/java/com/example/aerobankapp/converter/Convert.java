package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.Balance;

public interface Convert
{
    BalanceHistoryEntity getConversion(com.example.aerobankapp.workbench.history.BalanceHistory balanceHistory);
}
