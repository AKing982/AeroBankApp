package com.example.aerobankapp.services.builder;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.model.AccountNotification;
import org.springframework.stereotype.Component;

@Component
public class AccountNotificationEntityBuilderImpl implements EntityBuilder<AccountNotificationEntity, AccountNotification>
{

    @Override
    public AccountNotificationEntity createEntity(AccountNotification model) {
        AccountNotificationEntity entity = new AccountNotificationEntity();
        entity.setAccount(AccountEntity.builder().acctID(model.getAccountID()).build());
        entity.setRead(false);
        entity.setMessage(model.getMessage());
        entity.setTitle(model.getTitle());
        entity.setSevere(model.isSevere());
        entity.setPriority(model.getPriority());
        entity.setAccountNotificationCategory(model.getCategory());
        return entity;
    }
}
