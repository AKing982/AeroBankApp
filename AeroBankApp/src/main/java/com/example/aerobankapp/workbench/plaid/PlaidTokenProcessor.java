package com.example.aerobankapp.workbench.plaid;

import com.plaid.client.model.ItemPublicTokenExchangeRequest;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.model.LinkTokenCreateRequest;
import com.plaid.client.model.LinkTokenCreateResponse;

public interface PlaidTokenProcessor
{
    LinkTokenCreateResponse createLinkToken(String clientUserId) throws Exception;

    ItemPublicTokenExchangeResponse exchangeItemPublicToken(ItemPublicTokenExchangeRequest request);
}
