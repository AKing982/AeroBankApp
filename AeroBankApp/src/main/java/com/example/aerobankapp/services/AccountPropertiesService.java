package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.workbench.utilities.color.AccountPropertiesSelectorImpl;

import java.util.List;
import java.util.Set;

public interface AccountPropertiesService extends ServiceDAOModel<AccountPropertiesEntity>
{
    List<AccountPropertiesEntity> getAccountPropertiesByAcctID(int acctID);

    List<AccountPropertiesEntity> getAccountPropertiesByUserID(int userID);

    void saveAll(List<AccountPropertiesEntity> accountPropertiesEntities);

    void deleteAll(Set<AccountPropertiesEntity> accountPropertiesEntities);

    String getRandomImageUrl();
    String getRandomAcctColor();

    AccountPropertiesEntity buildAccountPropertiesEntity(AccountEntity accountEntity);

    List<AccountPropertiesEntity> getListOfAccountPropertiesFromAccountEntity(List<AccountEntity> accountEntities);
}
