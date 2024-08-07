package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.PlaidTransactionEntity;
import com.example.aerobankapp.entity.TransactionCriteriaEntity;
import com.example.aerobankapp.model.PlaidTransaction;
import com.example.aerobankapp.model.PlaidTransactionImport;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;

public class PlaidTransactionToTransactionCriteriaConverter implements ModelConverter<TransactionCriteriaEntity, PlaidTransactionImport>
{


    @Override
    public TransactionCriteriaEntity convert(PlaidTransactionImport model) {
        TransactionCriteriaEntity transactionCriteria = new TransactionCriteriaEntity();
        transactionCriteria.setConfirmationNumber(null);
        transactionCriteria.setReferenceNumber(model.getReferenceNumber());
        transactionCriteria.setAmount(model.getAmount());
        transactionCriteria.setPosted(model.getPosted());
        transactionCriteria.setDescription(model.getTransactionName());
        transactionCriteria.setTransactionStatus(TransactionStatus.COMPLETED);
        transactionCriteria.setNotificationsEnabled(false);
        return transactionCriteria;
    }
}
