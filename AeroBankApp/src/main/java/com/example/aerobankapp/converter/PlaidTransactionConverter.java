package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.PlaidTransactionEntity;
import com.example.aerobankapp.model.PlaidTransaction;
import com.example.aerobankapp.model.PlaidTransactionImport;
import org.springframework.stereotype.Component;

@Component
public class PlaidTransactionConverter implements ModelConverter<PlaidTransactionEntity, PlaidTransactionImport>
{

    @Override
    public PlaidTransactionEntity convert(PlaidTransactionImport model) {
        PlaidTransactionEntity entity = new PlaidTransactionEntity();
        entity.setAuthorizedDate(model.getTransactionDate());
        entity.setAmount(model.getAmount());
        entity.setMerchantName(model.getTransactionName());
        entity.setPending(model.isPending());
        entity.setExternalId(model.getTransactionId());
        entity.setExternalAcctID(model.getAcctID());
        entity.setDate(model.getTransactionDate());
        entity.setName(model.getTransactionName());
        return entity;
    }
}
