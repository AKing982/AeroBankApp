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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;

@Service
public class PlaidAccountManager extends AbstractPlaidDataManager {

    private AccountBaseToPlaidAccountConverter accountBaseToPlaidAccountConverter;
    private Logger LOGGER = LoggerFactory.getLogger(PlaidAccountManager.class);

    @Autowired
    public PlaidAccountManager(PlaidAccountsService plaidAccountsService, PlaidApi plaidApi) {
        super(plaidAccountsService, plaidApi);
        this.accountBaseToPlaidAccountConverter = new AccountBaseToPlaidAccountConverter();
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
       // assertAccountBaseListIsEmpty(accountBaseList);
        for(AccountBase accountBase : accountBaseList)
        {
            if(accountBase != null)
            {
                LOGGER.info("AccountBase: {}", accountBase);
                PlaidAccount convertedAccount = accountBaseToPlaidAccountConverter.convert(accountBase);
                if(convertedAccount != null)
                {
                    plaidAccounts.add(convertedAccount);
                }
                LOGGER.info("Converted Account: {}", convertedAccount);
            }

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
        validateUserID(userId);
        Optional<PlaidAccountsEntity> optional = getPlaidAccountEntityByUserId(userId);
        if(optional.isPresent())
        {
           String accessToken = getAccessTokenFromResponse(optional);
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

    public AccountsGetResponse getAllAccountsRetryResponse(AccountsGetRequest accountsGetRequest) throws IOException, InterruptedException {
        if(accountsGetRequest == null)
        {
            throw new NullPointerException("accountsGetRequest cannot be null");
        }

        return executeWithRetry(() ->
        {
            try
            {
                return plaidApi.accountsGet(accountsGetRequest).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

//        try
//        {
//            while(attempts < TOTAL_ATTEMPTS)
//            {
//                accountsGetResponse = plaidApi.accountsGet(accountsGetRequest).execute();
//                if(accountsGetResponse.isSuccessful() && accountsGetResponse.body() != null)
//                {
//                    return accountsGetResponse.body();
//                }
//                else
//                {
//                    attempts++;
//
//                    if(attempts == TOTAL_ATTEMPTS)
//                    {
//                        throw new PlaidAccountsGetResponseNullPointerException("Accounts Response is null");
//                    }
//                }
//            }
//
//        }catch(Exception ex)
//        {
//            throw ex;
//        }
    }


    public LinkTokenCreateResponse linkAccount(int userId, String linkToken) {
        return null;
    }

    public Boolean unlinkAccount(String acctId) {
        return null;
    }

    public AccountsGetResponse getBalancesByUserId(int userID)
    {
        return null;
    }
}
