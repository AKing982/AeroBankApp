package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.Optional;

public interface ExternalAccountsService extends ServiceDAOModel<ExternalAccountsEntity>
{
    ExternalAccountsEntity createExternalAccount(String externalAcctID, int acctID);

    Optional<ExternalAccountsEntity> getExternalAccount(String externalAcctID);
}
