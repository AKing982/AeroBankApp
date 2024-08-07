package com.example.aerobankapp.services.builder;

import com.example.aerobankapp.entity.AccountDetailsEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.model.AccountDetails;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailsEntityBuilderImpl implements EntityBuilder<AccountDetailsEntity, AccountDetails>
{

    @Override
    public AccountDetailsEntity createEntity(AccountDetails model) {
        AccountDetailsEntity accountDetailsEntity = new AccountDetailsEntity();
        accountDetailsEntity.setAccount(AccountEntity.builder().acctID(model.getAcctID()).balance(model.getBalance()).build());
        accountDetailsEntity.setAvailable(model.getAvailable());
        accountDetailsEntity.setPendingBalance(model.getPending());
        accountDetailsEntity.setBalance(model.getBalance());
        return accountDetailsEntity;
    }
}
