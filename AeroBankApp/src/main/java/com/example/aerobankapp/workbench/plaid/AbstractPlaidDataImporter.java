package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.model.PlaidImportResult;
import com.example.aerobankapp.services.ExternalAccountsService;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPlaidDataImporter
{
    protected ExternalAccountsService externalAccountsService;
    protected Map<String, String> subTypeToTypeCriteria = new HashMap<>();

    public AbstractPlaidDataImporter(ExternalAccountsService externalAccountsService)
    {
        this.externalAccountsService = externalAccountsService;
        initializeSubTypeToTypeMap();
    }

    protected void createAndSaveExternalAccountEntity(String externalAcctID, int acctID){
        ExternalAccountsEntity externalAccountsEntity = externalAccountsService.createExternalAccount(externalAcctID, acctID);
        externalAccountsService.save(externalAccountsEntity);
    }

    protected PlaidImportResult createPlaidImportResult(Object result, boolean isSuccess){
        return new PlaidImportResult(result, isSuccess);
    }

    private void initializeSubTypeToTypeMap(){
        subTypeToTypeCriteria.put("checking", "depository");
        subTypeToTypeCriteria.put("savings", "depository");
        subTypeToTypeCriteria.put("paypal", "credit");
        subTypeToTypeCriteria.put("student", "loan");
        subTypeToTypeCriteria.put("auto", "loan");
        subTypeToTypeCriteria.put("personal", "loan");
        subTypeToTypeCriteria.put("mortgage", "loan");
        subTypeToTypeCriteria.put("payable", "loan");
    }



}
