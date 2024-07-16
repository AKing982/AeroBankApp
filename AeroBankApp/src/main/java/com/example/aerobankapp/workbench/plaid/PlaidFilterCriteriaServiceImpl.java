package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.converter.AccountBaseToPlaidAccountBalancesConverter;
import com.example.aerobankapp.model.PlaidAccountBalances;
import com.example.aerobankapp.model.PlaidTransactionCriteria;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.Transaction;
import com.plaid.client.model.TransactionsGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaidFilterCriteriaServiceImpl implements PlaidFilterCriteriaService
{
    private AccountBaseToPlaidAccountBalancesConverter accountBaseToPlaidAccountBalancesConverter;

    public PlaidFilterCriteriaServiceImpl() {
        this.accountBaseToPlaidAccountBalancesConverter = new AccountBaseToPlaidAccountBalancesConverter();
    }

    @Override
    public Page<PlaidTransactionCriteria> getPlaidTransactionCriteriaFromResponse(Pageable pageable, TransactionsGetResponse transactionsGetResponse) {

        if(transactionsGetResponse == null || pageable == null) {
            throw new RuntimeException("TransactionsGetResponse or pageable is null");
        }

        List<Transaction> transactions = transactionsGetResponse.getTransactions();

        List<PlaidTransactionCriteria> allTransactions = convertTransactionListToCriteriaList(transactions);
        int start = (int) pageable.getOffset();
        int end = getPageableEnd(start, pageable, allTransactions);

        List<PlaidTransactionCriteria> paginatedTransactions = getPaginatedCriteriaList(allTransactions, start, end);
        return getPaginatedTransactionCriteria(paginatedTransactions, pageable);
    }

    private Page<PlaidTransactionCriteria> getPaginatedTransactionCriteria(List<PlaidTransactionCriteria> transactions, Pageable pageable) {
        return new PageImpl<>(transactions, pageable, transactions.size());
    }

    public int getPageableEnd(int start, Pageable pageable, List<PlaidTransactionCriteria> plaidTransactionCriteriaList) {
        return Math.min(start + pageable.getPageSize(), plaidTransactionCriteriaList.size());
    }

    public List<PlaidTransactionCriteria> convertTransactionListToCriteriaList(List<Transaction> transactions) {
        if(transactions == null)
        {
            throw new RuntimeException("Transactions is null");
        }

        List<PlaidTransactionCriteria> criteriaList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            PlaidTransactionCriteria criteria = buildPlaidTransactionCriteria(transaction);
            criteriaList.add(criteria);
        }
        return criteriaList;
    }

    public List<PlaidTransactionCriteria> getPaginatedCriteriaList(List<PlaidTransactionCriteria> transactions, int start, int end)
    {
        return transactions.subList(start, end);
    }

    public List<PlaidAccountBalances> getAccountBalancesForTransactions(TransactionsGetResponse transactionsGetResponse) {
        List<PlaidAccountBalances> accountBalances = new ArrayList<>();
        if(transactionsGetResponse == null || transactionsGetResponse.getTransactions() == null)
        {
            throw new RuntimeException("TransactionsGetResponse or getTransactions is null");
        }
        List<AccountBase> accounts = transactionsGetResponse.getAccounts();
        if(accounts == null || accounts.isEmpty())
        {
            throw new RuntimeException("Accounts list is null or empty");
        }

        for(AccountBase accountBase : accounts)
        {
            if(accountBase != null)
            {
                PlaidAccountBalances plaidAccountBalances = accountBaseToPlaidAccountBalancesConverter.convert(accountBase);
                if(plaidAccountBalances != null)
                {
                    accountBalances.add(plaidAccountBalances);
                }
            }
        }
        return accountBalances;
    }


    public PlaidTransactionCriteria buildPlaidTransactionCriteria(Transaction transaction)
    {
        if(transaction == null)
        {
            throw new RuntimeException("Transaction is null");
        }
        PlaidTransactionCriteria plaidTransactionCriteria = new PlaidTransactionCriteria();
        plaidTransactionCriteria.setMerchantName(transaction.getMerchantName());
        plaidTransactionCriteria.setDescription(transaction.getOriginalDescription());
        plaidTransactionCriteria.setDate(transaction.getDate());
        plaidTransactionCriteria.setAuthorizedDate(transaction.getAuthorizedDate());
        plaidTransactionCriteria.setPending(transaction.getPending());
        plaidTransactionCriteria.setExternalAcctID(transaction.getAccountId());
        plaidTransactionCriteria.setExternalId(transaction.getTransactionId());
        plaidTransactionCriteria.setAmount(BigDecimal.valueOf(transaction.getAmount()));
        return plaidTransactionCriteria;
    }
}
