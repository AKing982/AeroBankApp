package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.exceptions.PlaidAccessTokenNotFoundException;
import com.example.aerobankapp.model.SearchCriteria;
import com.example.aerobankapp.services.PlaidAccountsService;
import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetResponse;
import com.plaid.client.model.TransactionsSyncRequest;
import com.plaid.client.model.TransactionsSyncResponse;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlaidTransactionManagerImpl extends AbstractPlaidDataManager
{

    @Autowired
    public PlaidTransactionManagerImpl(PlaidAccountsService plaidAccountsService, PlaidApi plaidApi)
    {
        super(plaidAccountsService, plaidApi);
    }

    private TransactionsSyncRequest buildTransactionSyncRequest(String accessToken, String cursor)
    {
        return new TransactionsSyncRequest()
                .cursor(cursor)
                .accessToken(accessToken);
    }

    public List<TransactionsGetResponse> getTransactionsByAcctId(int acctID)
    {
        return null;
    }

    public List<TransactionsGetResponse> getPendingTransactionsByUserId(int userID)
    {
        return null;
    }

    public List<TransactionsGetResponse> searchTransactionsByCriteria(SearchCriteria searchCriteria)
    {
        return null;
    }

    public TransactionsSyncResponse getTransactionSyncResponse(int userID, String cursor)
    {
        return null;
    }

    public TransactionsSyncResponse getTransactionSyncRetryResponse(TransactionsSyncRequest transactionsSyncRequest)
    {
        return null;
    }

    public TransactionsGetResponse getTransactionResponse(int userID, LocalDate startDate, LocalDate endDate) throws IOException {
        if(userID < 1 || startDate == null || endDate == null)
        {
            throw new IllegalArgumentException("Transaction Request parameters are invalid");
        }
        // Does the user have an access token already?
        Optional<PlaidAccountsEntity> plaidAccountsEntityOptional = getPlaidAccountEntityByUserId(userID);
        if(plaidAccountsEntityOptional.isPresent())
        {
            // Fetch the access Token
            String accessToken = getAccessTokenFromResponse(plaidAccountsEntityOptional);
            if(accessToken == null)
            {
                throw new PlaidAccessTokenNotFoundException("Plaid access token not found");
            }

            // Build the TransactionsGetRequest
            TransactionsGetRequest transactionsGetRequest = buildTransactionsGetRequest(accessToken, startDate, endDate);

            Response<TransactionsGetResponse> response = plaidApi.transactionsGet(transactionsGetRequest).execute();
            if(!response.isSuccessful())
            {
                return getTransactionResponseWithRetry(transactionsGetRequest);
            }
            else
            {
                return plaidApi.transactionsGet(transactionsGetRequest).execute().body();
            }
        }
        else
        {
            throw new PlaidAccessTokenNotFoundException("Plaid access token not found");
        }
        // If the user does, then fetch the access token from the database
    }

    public TransactionsGetResponse getTransactionResponseWithRetry(TransactionsGetRequest transactionsGetRequest) throws IOException
    {
        if(transactionsGetRequest == null)
        {
            throw new IllegalArgumentException("TransactionsGetRequest cannot be null");
        }

        return executeWithRetry(() -> {
            try
            {
                return plaidApi.transactionsGet(transactionsGetRequest).execute();
            }catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        });
//        int attempts = 0;
//        Response<TransactionsGetResponse> response;
//        try
//        {
//            while(attempts < TOTAL_ATTEMPTS)
//            {
//                response = plaidApi.transactionsGet(transactionsGetRequest).execute();
//                if(response.isSuccessful() && response.body() != null)
//                {
//                    return response.body();
//                }
//                else
//                {
//                    attempts++;
//                    if(attempts == TOTAL_ATTEMPTS)
//                    {
//                        break;
//                    }
//                }
//            }
//        }catch(Exception e)
//        {
//            throw e;
//        }
//        throw new IOException("Failed to get transactions response");
    }

    private TransactionsGetRequest buildTransactionsGetRequest(String accessToken, LocalDate startDate, LocalDate endDate) {
        return new TransactionsGetRequest()
                .accessToken(accessToken)
                .startDate(startDate)
                .endDate(endDate);
    }
}
