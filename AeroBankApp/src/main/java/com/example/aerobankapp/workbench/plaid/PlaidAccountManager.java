package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.AccountBaseToPlaidAccountConverter;
import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.LinkTokenCreateResponse;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

@Service
public class PlaidAccountManager extends AbstractPlaidDataManager {

    private AccountBaseToPlaidAccountConverter accountBaseToPlaidAccountConverter;

    @Autowired
    public PlaidAccountManager(PlaidAccountsService plaidAccountsService, PlaidApi plaidApi, AccountBaseToPlaidAccountConverter accountBaseToPlaidAccountConverter) {
        super(plaidAccountsService, plaidApi);
        this.accountBaseToPlaidAccountConverter = accountBaseToPlaidAccountConverter;
    }

    public AccountsGetRequest buildAccountsGetRequest(String accessToken) {
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest();
        accountsGetRequest.setAccessToken(accessToken);
        return accountsGetRequest;
    }

    public Set<PlaidAccount> getPlaidAccountsSetFromResponse(final List<AccountBase> accountBaseList)
    {
        Set<PlaidAccount> plaidAccounts = new HashSet<>();
        assertAccountBaseListIsNull(accountBaseList);
     //   assertAccountBaseListIsEmpty(accountBaseList);

        for(AccountBase accountBase : accountBaseList)
        {
            PlaidAccount convertedAccount = accountBaseToPlaidAccountConverter.convert(accountBase);
            plaidAccounts.add(convertedAccount);
        }
        return plaidAccounts;
    }

    private void assertAccountBaseListIsEmpty(final List<AccountBase> accountBaseList)
    {
        Assert.isTrue(CollectionUtils.isEmpty(accountBaseList), "accountBaseList cannot be empty");
    }

    private void assertAccountBaseListIsNull(final List<AccountBase> accountBaseList)
    {
        Assert.notNull(accountBaseList, "accountBaseList cannot be null");
    }

    public AccountsGetResponse getAllAccounts(final int userId) throws IOException
    {
        if(userId < 1)
        {
            throw new InvalidUserIDException("Invalid user ID");
        }
        Optional<PlaidAccountsEntity> optional = plaidAccountsService.findPlaidAccountEntityByUserId(userId);
        if(optional.isPresent())
        {
           String accessToken = optional.get().getAccessToken();
           if(accessToken == null || accessToken.isEmpty())
           {
               throw new PlaidAccessTokenNotFoundException("Plaid access token not found");
           }
           AccountsGetRequest accountsGetRequest = buildAccountsGetRequest(accessToken);
           AccountsGetResponse response = plaidApi.accountsGet(accountsGetRequest).execute().body();
           if(response == null)
           {
               throw new PlaidAccountsGetResponseNullPointerException("Accounts Response is null");
           }
           else
           {
               return response;
           }
        }
        else
        {
            throw new PlaidAccountNotFoundException("Plaid account not found");
        }
    }

    public AccountsGetResponse getAllAccountsRetryResponse(AccountsGetRequest accountsGetRequest)
    {
        return null;
    }

    public AccountsGetResponse getAccountById(String accountId) {
        return null;
    }

    public LinkTokenCreateResponse linkAccount(int userId, String linkToken) {
        return null;
    }

    public Boolean unlinkAccount(String acctId) {
        return null;
    }

    public AccountsGetResponse getAccountBalancesForAcctID(String acctId)
    {
        return null;
    }


}
