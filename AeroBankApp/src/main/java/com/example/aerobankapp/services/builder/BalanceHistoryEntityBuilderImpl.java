package com.example.aerobankapp.services.builder;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.BalanceHistory;
import org.springframework.stereotype.Component;

@Component
public class BalanceHistoryEntityBuilderImpl implements EntityBuilder<BalanceHistoryEntity, BalanceHistory>
{

    @Override
    public BalanceHistoryEntity createEntity(BalanceHistory model) {
        BalanceHistoryEntity balanceHistoryEntity = new BalanceHistoryEntity();
        balanceHistoryEntity.setPostBalance(model.getNewBalance());
        balanceHistoryEntity.setPreBalance(model.getCurrentBalance());
        balanceHistoryEntity.setAccount(AccountEntity.builder().acctID(model.getAccountID()).build());
        balanceHistoryEntity.setPosted(model.getPosted());
        balanceHistoryEntity.setTransactionType(model.getTransactionType().getCode());
        return balanceHistoryEntity;
    }
}
