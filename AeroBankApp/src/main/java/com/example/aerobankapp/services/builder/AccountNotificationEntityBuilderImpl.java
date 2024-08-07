package com.example.aerobankapp.services.builder;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.model.AccountNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountNotificationEntityBuilderImpl implements EntityBuilder<AccountNotificationEntity, AccountNotification>
{
    private Logger LOGGER = LoggerFactory.getLogger(AccountNotificationEntityBuilderImpl.class);

    @Override
    public AccountNotificationEntity createEntity(AccountNotification model) {
        if(model == null){
            LOGGER.error("Unable to create AccountNotification entity because model is null");
            throw new IllegalArgumentException("Unable to create AccountNotification entity because model is null");
        }
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
