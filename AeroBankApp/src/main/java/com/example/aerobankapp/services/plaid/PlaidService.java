package com.example.aerobankapp.services.plaid;

import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.time.LocalDate;
import java.util.Arrays;

@Service
public class PlaidService
{
    private final PlaidApi plaidApi;

    private Logger LOGGER = LoggerFactory.getLogger(PlaidService.class);

    @Autowired
    public PlaidService(PlaidApi plaidApi)
    {
        this.plaidApi = plaidApi;
    }

    /**
     * Creates a link token for a given client user ID.
     *
     * @param clientUserId The ID of the client user.
     * @return A LinkTokenCreateResponse object representing the link token.
     * @throws Exception if an error occurs while creating the link token.
     */
    public LinkTokenCreateResponse createLinkToken(String clientUserId) throws Exception
    {
        try
        {
            LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                    .user(new LinkTokenCreateRequestUser().clientUserId(clientUserId))
                    .clientName("AeroBankApp")
                    .products(Arrays.asList(Products.AUTH, Products.TRANSACTIONS))
                    .countryCodes(Arrays.asList(CountryCode.US))
                    .language("en");

            Response<LinkTokenCreateResponse> response = plaidApi.linkTokenCreate(request).execute();

            if (!response.isSuccessful())
            {
                LOGGER.error("Error creating link token. Code: {}, Message: {}", response.code(), response.message());
                throw new Exception(response.message());
            }

            LinkTokenCreateResponse linkTokenCreateResponse = response.body();
            if(linkTokenCreateResponse == null)
            {
                LOGGER.error("Link token response is null");
                throw new Exception("Link token response is null");
            }
            return linkTokenCreateResponse;

        }catch(Exception e)
        {
            LOGGER.error("Exception while creating link token", e);
            throw e;
        }
    }

    /**
     * Exchanges a public token for an access token and item ID.
     *
     * @param publicToken The public token to exchange.
     * @return An ItemPublicTokenExchangeResponse object containing the access token and item ID.
     * @throws Exception if an error occurs while exchanging the public token.
     */
    public ItemPublicTokenExchangeResponse exchangePublicToken(String publicToken) throws Exception
    {
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest()
                .publicToken(publicToken);

        return plaidApi.itemPublicTokenExchange(request).execute().body();
    }

    /**
     * Retrieves the account balances for a given access token.
     *
     * @param accessToken The access token used to authenticate the request.
     * @return An AccountsGetResponse object representing the account balances.
     * @throws Exception if an error occurs while retrieving the account balances.
     */
    public AccountsGetResponse getAccountBalances(String accessToken) throws Exception
    {
        AccountsGetRequest request = new AccountsGetRequest()
                .accessToken(accessToken);
        return plaidApi.accountsGet(request).execute().body();
    }

    /**
     * Retrieves the transactions for a given access token within a specified date range.
     *
     * @param accessToken The access token used to authenticate the request.
     * @param startDate The start date of the transaction range.
     * @param endDate The end date of the transaction range.
     * @return A TransactionsGetResponse object representing the transactions.
     * @throws Exception if an error occurs while retrieving the transactions.
     */
    public TransactionsGetResponse getTransactions(String accessToken, LocalDate startDate, LocalDate endDate) throws Exception
    {
        TransactionsGetRequest request = new TransactionsGetRequest()
                .accessToken(accessToken)
                .startDate(startDate)
                .endDate(endDate);
        return plaidApi.transactionsGet(request).execute().body();
    }

    /**
     * Synchronizes transactions for a given access token with a specified cursor.
     *
     * @param accessToken The access token used to authenticate the request.
     * @param cursor The cursor indicating the position to start syncing transactions from.
     * @return A TransactionsSyncResponse object representing the synchronized transactions.
     * @throws Exception if an error occurs while synchronizing the transactions.
     */
    public TransactionsSyncResponse syncTransactions(String accessToken, String cursor) throws Exception
    {
        TransactionsSyncRequest request = new TransactionsSyncRequest()
                .accessToken(accessToken)
                .cursor(cursor);
        return plaidApi.transactionsSync(request).execute().body();
    }



}
