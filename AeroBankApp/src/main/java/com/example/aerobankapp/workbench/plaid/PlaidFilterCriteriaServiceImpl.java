package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.model.PlaidTransactionCriteria;
import com.plaid.client.model.TransactionsGetResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PlaidFilterCriteriaServiceImpl implements PlaidFilterCriteriaService
{

    @Override
    public Page<PlaidTransactionCriteria> getPlaidTransactionCriteriaFromResponse(TransactionsGetResponse transactionsGetResponse, Pageable pageable) {
        return null;
    }

    public List<PlaidTransactionCriteria> convertResponseToCriteria(TransactionsGetResponse transactionsGetResponse) {
        return null;
    }

    public List<PlaidTransactionCriteria> getPaginatedCriteriaList(List<PlaidTransactionCriteria> transactions)
    {
        return null;
    }

    public PlaidTransactionCriteria buildPlaidTransactionCriteria()
    {
        return null;
    }
}
