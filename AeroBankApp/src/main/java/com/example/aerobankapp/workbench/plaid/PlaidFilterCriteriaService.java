package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.model.PlaidTransactionCriteria;
import com.plaid.client.model.TransactionsGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PlaidFilterCriteriaService
{
    Page<PlaidTransactionCriteria> getPlaidTransactionCriteriaFromResponse(Pageable pageable, TransactionsGetResponse transactionsGetResponse);
}
