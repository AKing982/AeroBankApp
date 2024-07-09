package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.PlaidApiResponseException;
import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.StringUtil;
import com.plaid.client.request.PlaidApi;
import org.springframework.util.StringUtils;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractPlaidDataManager
{
    protected PlaidAccountsService plaidAccountsService;
    protected PlaidApi plaidApi;
    protected final int TOTAL_ATTEMPTS = 5;

    public AbstractPlaidDataManager(PlaidAccountsService plaidAccountsService, PlaidApi plaidApi)
    {
        this.plaidAccountsService = plaidAccountsService;
        this.plaidApi = plaidApi;
    }

    protected void validateUserID(int userID)
    {
        if(userID < 1)
        {
            throw new InvalidUserIDException("Invalid user ID");
        }
    }

    protected Optional<PlaidAccountsEntity> getPlaidAccountEntityByUserId(int userID)
    {
        return plaidAccountsService.findPlaidAccountEntityByUserId(userID);
    }

    protected String getAccessTokenFromResponse(Optional<PlaidAccountsEntity> plaidAccountsEntity)
    {
        if(plaidAccountsEntity.isPresent())
        {
            if(plaidAccountsEntity.get().getAccessToken() != null)
            {
                return plaidAccountsEntity.get().getAccessToken();
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
