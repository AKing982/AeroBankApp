package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.workbench.utilities.color.AccountPropertiesSelectorImpl;

import java.util.List;

public interface AccountPropertiesService extends ServiceDAOModel<AccountPropertiesEntity>
{
    List<AccountPropertiesEntity> getAccountPropertiesByAcctID(int acctID);

    void saveAll(List<AccountPropertiesEntity> accountPropertiesEntities);

    AccountPropertiesEntity buildAccountPropertiesEntity(AccountEntity accountEntity);

    List<AccountPropertiesEntity> getListOfAccountPropertiesFromAccountEntity(List<AccountEntity> accountEntities);
}
