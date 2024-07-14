package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.model.PlaidTransactionCriteria;
import com.plaid.client.model.TransactionsGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaidFilterCriteriaService
{
    Page<PlaidTransactionCriteria> getPlaidTransactionCriteriaFromResponse(TransactionsGetResponse transactionsGetResponse, Pageable pageable);
}
