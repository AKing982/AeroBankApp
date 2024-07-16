package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.PlaidLinkEntity;
import com.example.aerobankapp.exceptions.PlaidAccessTokenNotFoundException;
import com.example.aerobankapp.model.SearchCriteria;
import com.example.aerobankapp.model.TransactionStatement;
import com.example.aerobankapp.services.PlaidLinkService;
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
    public PlaidTransactionManagerImpl(PlaidLinkService plaidLinkService, PlaidApi plaidApi)
    {
        super(plaidLinkService, plaidApi);
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

    public List<TransactionStatement> getTransactionStatementsFromResponse(TransactionsGetResponse transactionsGetResponse)
    {
        return null;
    }

    public TransactionsGetResponse getTransactionResponse(int userID, LocalDate startDate, LocalDate endDate) throws IOException {
        if(userID < 1 || startDate == null || endDate == null)
        {
            throw new IllegalArgumentException("Transaction Request parameters are invalid");
        }
        // Does the user have an access token already?
        Optional<PlaidLinkEntity> plaidLinkEntityOptional = getPlaidLinkEntityByUserId(userID);
        if(plaidLinkEntityOptional.isPresent())
        {
            // Fetch the access Token
            String accessToken = getAccessTokenFromResponse(plaidLinkEntityOptional);
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
    }

    private TransactionsGetRequest buildTransactionsGetRequest(String accessToken, LocalDate startDate, LocalDate endDate) {
        return new TransactionsGetRequest()
                .accessToken(accessToken)
                .startDate(startDate)
                .endDate(endDate);
    }
}
