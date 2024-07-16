package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.PlaidLinkEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.PlaidApiResponseException;
import com.example.aerobankapp.services.PlaidLinkService;
import com.plaid.client.request.PlaidApi;
import retrofit2.Response;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractPlaidDataManager
{
    protected PlaidLinkService plaidLinkService;
    protected PlaidApi plaidApi;
    protected final int TOTAL_ATTEMPTS = 5;

    public AbstractPlaidDataManager(PlaidLinkService plaidLinkService, PlaidApi plaidApi)
    {
        this.plaidLinkService = plaidLinkService;
        this.plaidApi = plaidApi;
    }

    protected void validateUserID(int userID)
    {
        if(userID < 1)
        {
            throw new InvalidUserIDException("Invalid user ID");
        }
    }


    protected Optional<PlaidLinkEntity> getPlaidLinkEntityByUserId(int userID)
    {
        return plaidLinkService.findPlaidLinkEntityByUserId(userID);
    }

    protected String getAccessTokenFromResponse(Optional<PlaidLinkEntity> plaidLinkEntity)
    {
        if(plaidLinkEntity.isPresent())
        {
            if(plaidLinkEntity.get().getAccessToken() != null)
            {
                return plaidLinkEntity.get().getAccessToken();
            }
        }
        return "";
    }

    protected <T extends Response<U>, U> U executeWithRetry(Supplier<T> requestSupplier)
    {
        U responseBody = null;
        int attempts = 0;
        while(attempts < TOTAL_ATTEMPTS && responseBody == null)
        {
            T response = requestSupplier.get();
            if(response.isSuccessful() && response.body() != null)
            {
                responseBody = response.body();
            }
            else
            {
                attempts++;
            }
        }
        if(responseBody == null)
        {
            throw new PlaidApiResponseException("Failed to get a successful response after " + TOTAL_ATTEMPTS + " attempts.");
        }
        return responseBody;
    }


}
