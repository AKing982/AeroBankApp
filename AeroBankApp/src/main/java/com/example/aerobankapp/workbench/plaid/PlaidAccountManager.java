package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.AccountBaseToPlaidAccountConverter;
import com.example.aerobankapp.entity.PlaidLinkEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.PlaidAccountBalances;
import com.example.aerobankapp.services.PlaidLinkService;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaidAccountManager extends AbstractPlaidDataManager {

    private AccountBaseToPlaidAccountConverter accountBaseToPlaidAccountConverter;
    private Logger LOGGER = LoggerFactory.getLogger(PlaidAccountManager.class);

    @Autowired
    public PlaidAccountManager(PlaidLinkService plaidLinkService, PlaidApi plaidApi) {
        super(plaidLinkService, plaidApi);
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

    public AccountsGetResponse getAllAccounts(final int userId) throws IOException, InterruptedException {
        validateUserID(userId);
        Optional<PlaidLinkEntity> optional = getPlaidLinkEntityByUserId(userId);
        if(optional.isPresent())
        {
           String accessToken = getAccessTokenFromResponse(optional);
           if(accessToken == null || accessToken.isEmpty())
           {
               throw new PlaidAccessTokenNotFoundException("Plaid access token not found");
           }
           AccountsGetRequest accountsGetRequest = buildAccountsGetRequest(accessToken);
           Response<AccountsGetResponse> response = plaidApi.accountsGet(accountsGetRequest).execute();
           if(response == null)
           {
               throw new PlaidAccountsGetResponseNullPointerException("Accounts Response is null");
           }
           else
           {
                return getAllAccountsRetryResponse(accountsGetRequest);
           }
        }
        else
        {
            throw new PlaidAccountNotFoundException("Plaid Link not found.");
        }
    }

    public AccountsGetResponse getAllAccountsRetryResponse(final AccountsGetRequest accountsGetRequest) throws IOException, InterruptedException {
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
    }

    public TreeMap<Integer, Collection<PlaidAccountBalances>> getPlaidBalancesTreeMap(final List<PlaidAccountBalances> plaidAccountBalances)
    {
        TreeMap<Integer, Collection<PlaidAccountBalances>> plaidBalancesTreeMap = new TreeMap<>();
        if(plaidAccountBalances == null)
        {
            throw new NullPointerException("plaidAccountBalances cannot be null");
        }
        for(PlaidAccountBalances plaidAccountBalance : plaidAccountBalances)
        {
            if(plaidAccountBalance != null)
            {
                Integer userId = plaidAccountBalance.getUserId();
                plaidBalancesTreeMap.put(userId, plaidAccountBalances);
            }
        }
        return plaidBalancesTreeMap;
    }

    private PlaidAccountBalances createPlaidAccountBalances(AccountBase model, int userID)
    {
        PlaidAccountBalances plaidAccountBalances = new PlaidAccountBalances();
        plaidAccountBalances.setAvailableBalance(BigDecimal.valueOf(model.getBalances().getAvailable()));
        plaidAccountBalances.setCurrentBalance(BigDecimal.valueOf(model.getBalances().getCurrent()));
        plaidAccountBalances.setAccountId(model.getAccountId());
        plaidAccountBalances.setUserId(userID);
        return plaidAccountBalances;
    }

    public List<PlaidAccountBalances> getBalancesByUserId(int userID) throws IOException, InterruptedException {
        validateUserID(userID);
        AccountsGetResponse accountsGetResponse = getAllAccounts(userID);
        return accountsGetResponse.getAccounts().stream()
                .map(accountBase -> createPlaidAccountBalances(accountBase, userID))
                .collect(Collectors.toList());
    }
}
