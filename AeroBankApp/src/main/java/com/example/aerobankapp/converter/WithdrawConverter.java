package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.workbench.transactions.Withdraw;

import java.time.LocalDate;

public class WithdrawConverter implements EntityToModelConverter<WithdrawEntity, Withdraw>
{
    public WithdrawConverter(){

    }

    @Override
    public Withdraw convert(WithdrawEntity entity)
    {
        Withdraw withdraw = new Withdraw();
        withdraw.setId(entity.getWithdrawID());
        withdraw.setFromAccountID(entity.getAccount().getAcctID());
        withdraw.setAmount(entity.getTransactionCriteria().getAmount());
        withdraw.setDescription(entity.getTransactionCriteria().getDescription());
        withdraw.setPosted(LocalDate.now());
        return withdraw;
    }
}
