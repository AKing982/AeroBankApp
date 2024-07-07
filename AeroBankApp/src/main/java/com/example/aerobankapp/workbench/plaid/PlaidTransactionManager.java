package com.example.aerobankapp.workbench.plaid;

import com.plaid.client.model.TransactionsGetResponse;

import java.time.LocalDate;

public interface PlaidTransactionManager
{
    TransactionsGetResponse getTransactionResponse(String accessToken, LocalDate startDate, LocalDate endDate);
}
