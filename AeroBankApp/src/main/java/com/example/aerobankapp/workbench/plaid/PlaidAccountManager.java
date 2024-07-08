package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.LinkTokenCreateResponse;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaidAccountManager extends AbstractPlaidDataManager {

    @Autowired
    public PlaidAccountManager(PlaidAccountsService plaidAccountsService, PlaidApi plaidApi) {
        super(plaidAccountsService, plaidApi);
    }

    public AccountsGetRequest buildAccountsGetRequest(String accessToken, String clientId, String secret) {
        AccountsGetRequest accountsGetRequest = new AccountsGetRequest();
        accountsGetRequest.setAccessToken(accessToken);
        accountsGetRequest.setClientId(clientId);
        accountsGetRequest.setSecret(secret);
        return accountsGetRequest;
    }

    public List<AccountsGetResponse> getAllAccounts(int userId) {
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
