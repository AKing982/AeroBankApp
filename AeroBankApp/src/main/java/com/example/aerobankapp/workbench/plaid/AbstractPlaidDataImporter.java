package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.UserNotFoundException;
import com.example.aerobankapp.model.PlaidImportResult;
import com.example.aerobankapp.services.ExternalAccountsService;
import com.example.aerobankapp.services.PlaidLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPlaidDataImporter
{
    protected ExternalAccountsService externalAccountsService;
    protected PlaidLinkService plaidLinkService;
    protected Map<String, String> subTypeToTypeCriteria = new HashMap<>();
    private Logger LOGGER = LoggerFactory.getLogger(AbstractPlaidDataImporter.class);

    public AbstractPlaidDataImporter(ExternalAccountsService externalAccountsService,
                                     PlaidLinkService plaidLinkService)
    {
        this.externalAccountsService = externalAccountsService;
        this.plaidLinkService = plaidLinkService;
        initializeSubTypeToTypeMap();
    }

    protected void createAndSaveExternalAccountEntity(String externalAcctID, int acctID){
        ExternalAccountsEntity externalAccountsEntity = externalAccountsService.createExternalAccount(externalAcctID, acctID);
        externalAccountsService.save(externalAccountsEntity);
    }

    protected PlaidImportResult createPlaidImportResult(Object result, boolean isSuccess){
        return new PlaidImportResult(result, isSuccess);
    }

    protected boolean checkPlaidAccessToken(final UserEntity userEntity)
    {
        if(userEntity == null)
        {
            throw new IllegalArgumentException("User Cannot be found.");
        }

        int userID = userEntity.getUserID();

        try
        {
            return plaidLinkService.hasPlaidLink(userID);

        }catch(Exception e)
        {
            LOGGER.error("There was an error validating the plaid access token for userID: {}", userID, e);
            return false;
        }
    }

    protected void validateUser(UserEntity user)
    {
        if(user == null)
        {
            throw new UserNotFoundException("User not found");
        }
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
