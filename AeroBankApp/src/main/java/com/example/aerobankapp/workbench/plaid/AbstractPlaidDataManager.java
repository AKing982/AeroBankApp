package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.request.PlaidApi;

import java.util.Optional;

public abstract class AbstractPlaidDataManager
{
    protected PlaidAccountsService plaidAccountsService;
    protected PlaidApi plaidApi;

    public AbstractPlaidDataManager(PlaidAccountsService plaidAccountsService, PlaidApi plaidApi)
    {
        this.plaidAccountsService = plaidAccountsService;
        this.plaidApi = plaidApi;
    }

    protected Optional<PlaidAccountsEntity> getPlaidAccountEntityByUserId(int userID)
    {
        return plaidAccountsService.findPlaidAccountEntityByUserId(userID);
    }
}
