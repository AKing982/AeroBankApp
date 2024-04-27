package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountUserEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface AccountUsersEntityService extends ServiceDAOModel<AccountUserEntity>
{
    void saveAll(List<AccountUserEntity> accountUserEntityList);

    AccountUserEntity buildAccountUserEntity(AccountEntity accountEntity, UserEntity user);

    List<AccountUserEntity> getAccountUserEntityList(List<AccountEntity> accountEntities, UserEntity user);
}
