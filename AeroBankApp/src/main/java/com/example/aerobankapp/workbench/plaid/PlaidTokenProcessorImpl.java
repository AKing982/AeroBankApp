package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.exceptions.InvalidLinkTokenRequestException;
import com.example.aerobankapp.services.plaid.PlaidService;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;

@Component
public class PlaidTokenProcessorImpl implements PlaidTokenProcessor
{
    private PlaidApi plaidApi;
    private final int RETRY_ATTEMPTS = 3;
    private Logger LOGGER = LoggerFactory.getLogger(PlaidTokenProcessor.class);

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
    public LinkTokenCreateResponse createLinkToken(String clientUserId) throws Exception{

        validateClientUserId(clientUserId);
        LinkTokenCreateRequest request = buildLinkTokenRequest(clientUserId);
        validateLinkTokenCreateRequest(request);

        return getLinkTokenResponseWithRetry(request);
    }


    public LinkTokenCreateResponse getLinkTokenResponseWithRetry(LinkTokenCreateRequest request) throws IOException, InterruptedException {
        if(request == null)
        {
            throw new InvalidLinkTokenRequestException("Link token request is null");
        }
        Response<LinkTokenCreateResponse> response;
        int retryAttempts = 0;
        return plaidApi.linkTokenCreate(request).execute().body();
//        while(true)
//        {
//            try
//            {
//                response = getLinkTokenCreateResponse(request);
//                if(response.isSuccessful() && response.body() != null)
//                {
//                    return response.body();
//                }
//                else
//                {
//                    currAttempts++;
//                    if(currAttempts < RETRY_ATTEMPTS)
//                    {
//                        Thread.sleep(100);
//                    }
//                    else
//                    {
//                        throw new IllegalArgumentException("Failed to create link token");
//                    }
//                }
//            }catch(Exception e) {
//                currAttempts++;
//                if(currAttempts < RETRY_ATTEMPTS)
//                {
//                    Thread.sleep(1000);
//                }
//                else
//                {
//                    throw e;
//                }
//            }
//        }
    }

    private void validateClientUserId(String clientUserId)
    {
        if(clientUserId == null || clientUserId.isEmpty())
        {
            throw new IllegalArgumentException("clientUserId cannot be null or empty");
        }
    }

    private void validateLinkTokenCreateRequest(LinkTokenCreateRequest request)
    {
        if(request == null)
        {
            throw new IllegalArgumentException("request cannot be null");
        }
    }

    public Response<LinkTokenCreateResponse> getLinkTokenCreateResponse(LinkTokenCreateRequest createRequest) throws IOException
    {
        LOGGER.info("LinkTokenCreateRequest: {}", createRequest);
        try
        {
            Call<LinkTokenCreateResponse> call = plaidApi.linkTokenCreate(createRequest);
            if(call != null)
            {
                return call.execute();
            }

        }catch(Exception e)
        {
            LOGGER.error("There was an error retrieving the link token create", e);
        }
        return null;
    }

    @Override
    public ItemPublicTokenExchangeResponse exchangeItemPublicToken(ItemPublicTokenExchangeRequest request) {
        return null;
    }
}
