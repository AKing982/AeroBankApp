package com.example.aerobankapp.converter;

import com.example.aerobankapp.model.PlaidTransaction;
import com.plaid.client.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionToPlaidTransactionConverter implements ModelConverter<PlaidTransaction, Transaction>
{

    @Override
    public PlaidTransaction convert(Transaction model) {
        PlaidTransaction plaidTransaction = new PlaidTransaction();
        plaidTransaction.setTransactionId(model.getTransactionId());
        plaidTransaction.setTransactionName(model.getName());
        plaidTransaction.setAccountId(model.getAccountId());
        plaidTransaction.setTransactionId(model.getTransactionId());
        plaidTransaction.setAmount(BigDecimal.valueOf(model.getAmount()));
        plaidTransaction.setPending(model.getPending());
        plaidTransaction.setDate(model.getDate());
        plaidTransaction.setCategories(model.getCategory());
        return plaidTransaction;
    }
}
