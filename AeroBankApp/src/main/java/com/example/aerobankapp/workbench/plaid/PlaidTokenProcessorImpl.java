package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.services.plaid.PlaidService;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PlaidTokenProcessorImpl implements PlaidTokenProcessor
{
    private PlaidApi plaidApi;

    @Autowired
    public PlaidTokenProcessorImpl(PlaidApi plaidApi)
    {
        this.plaidApi = plaidApi;
    }

    public LinkTokenCreateRequest buildLinkTokenRequest(String clientUserId)
    {
        return new LinkTokenCreateRequest()
                .user(new LinkTokenCreateRequestUser().clientUserId(clientUserId))
                .clientName("Utah Kings Credit Union")
                .products(Arrays.asList(Products.AUTH, Products.TRANSACTIONS, Products.STATEMENTS, Products.RECURRING_TRANSACTIONS))
                .countryCodes(Arrays.asList(CountryCode.US))
                .language("en");
    }

    @Override
    public LinkTokenCreateResponse createLinkToken(String clientUserId)
    {
        if(clientUserId == null || clientUserId.isEmpty())
        {
            throw new IllegalArgumentException("clientUserId cannot be null or empty");
        }

        LinkTokenCreateRequest request = buildLinkTokenRequest(clientUserId);
        if(request == null)
        {
            throw new IllegalArgumentException("request cannot be null");
        }
        return null;
    }

    @Override
    public ItemPublicTokenExchangeResponse exchangeItemPublicToken(ItemPublicTokenExchangeRequest request) {
        return null;
    }
}
