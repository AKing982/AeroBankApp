package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaidAccountManager extends AbstractPlaidDataManager
{
    @Autowired
    public PlaidAccountManager(PlaidAccountsService plaidAccountsService, PlaidApi plaidApi)
    {
        super(plaidAccountsService, plaidApi);
    }


}
