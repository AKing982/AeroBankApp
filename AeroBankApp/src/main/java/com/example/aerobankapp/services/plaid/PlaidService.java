package com.example.aerobankapp.services.plaid;

import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;


public class PlaidService
{
    private PlaidApi plaidApi;

    public PlaidService(PlaidApi plaidApi)
    {
        this.plaidApi = plaidApi;
    }

    public LinkTokenCreateResponse createLinkToken(String clientUserId) throws Exception
    {
        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .user(new LinkTokenCreateRequestUser().clientUserId(clientUserId))
                .clientName("AeroBankApp")
                .products(Arrays.asList(Products.AUTH, Products.TRANSACTIONS))
                .countryCodes(Arrays.asList(CountryCode.US))
                .language("en");
        return plaidApi.linkTokenCreate(request).execute().body();
    }

    public ItemPublicTokenExchangeResponse exchangePublicToken(String publicToken) throws Exception
    {
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest()
                .publicToken(publicToken);

        return plaidApi.itemPublicTokenExchange(request).execute().body();
    }

}
