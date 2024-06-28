package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.Optional;

public interface PlaidAccountsService extends ServiceDAOModel<PlaidAccountsEntity>
{
    PlaidAccountsEntity buildPlaidAccountsEntity(String access_token, String item_id, int userID, String institutionName);

    Boolean hasPlaidAccount(int userID);

    Optional<PlaidAccountsEntity> findPlaidAccountEntityByUserId(int userID);
}
