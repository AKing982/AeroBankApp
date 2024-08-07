package com.example.aerobankapp.workbench.plaid;

import com.plaid.client.model.ItemPublicTokenExchangeRequest;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.model.LinkTokenCreateRequest;
import com.plaid.client.model.LinkTokenCreateResponse;

import java.io.IOException;

/**
 * The PlaidTokenProcessor interface provides methods for creating link tokens and exchanging public tokens.
 */
public interface PlaidTokenProcessor
{
    LinkTokenCreateResponse createLinkToken(String clientUserId) throws Exception;

    ItemPublicTokenExchangeResponse exchangePublicToken(String publicToken) throws IOException, InterruptedException;
}
