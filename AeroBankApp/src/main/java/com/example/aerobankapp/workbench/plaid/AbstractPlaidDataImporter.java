package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.PlaidLinkEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.PlaidAccessTokenNotFoundException;
import com.example.aerobankapp.exceptions.UserNotFoundException;
import com.example.aerobankapp.model.PlaidImportResult;
import com.example.aerobankapp.services.ExternalAccountsService;
import com.example.aerobankapp.services.PlaidLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

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

    public Map<String, Integer> getSingleSysAndPlaidAcctIdMap(final ExternalAccountsEntity externalAccountsEntity) {
        if(externalAccountsEntity == null)
        {
            throw new IllegalArgumentException("External Accounts entity cannot be null.");
        }
        Map<String, Integer> pairedAcctIds = new LinkedHashMap<>();
        String externalAcctID = externalAccountsEntity.getExternalAcctID();
        AccountEntity account = externalAccountsEntity.getAccount();
        if(account == null)
        {
            return new HashMap<>();
        }
        int sysAcctID = externalAccountsEntity.getAccount().getAcctID();
        pairedAcctIds.put(externalAcctID, sysAcctID);

        return pairedAcctIds;
    }


    protected void createAndSaveExternalAccountEntity(String externalAcctID, int acctID){
         try
         {
             ExternalAccountsEntity externalAccountsEntity = externalAccountsService.createExternalAccount(externalAcctID, acctID);
             LOGGER.info("Created ExternalAccountEntity: {}", externalAccountsEntity.toString());
             externalAccountsService.save(externalAccountsEntity);
             LOGGER.info("Saved ExternalAccountEntity: {}", externalAccountsEntity.toString());

         }catch(Exception e)
         {
             LOGGER.error("There was an error creating external account entity", e);
         }
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
        boolean isPresent = false;
        try
        {
            Optional<PlaidLinkEntity> plaidLinkEntityOptional = plaidLinkService.findPlaidLinkEntityByUserId(userID);
            if(plaidLinkEntityOptional.isPresent())
            {
                String accessToken = plaidLinkEntityOptional.get().getAccessToken();
                if(accessToken != null && !accessToken.isEmpty())
                {
                    isPresent = true;
                }
                else
                {
                    LOGGER.warn("No access token found for user ID: " + userID);
                    throw new PlaidAccessTokenNotFoundException("No Access Token found for user ID: " + userID);
                }
            }
        }catch(Exception e)
        {
            LOGGER.error("There was an error validating the plaid access token for userID: {}", userID, e);
            return false;
        }
        return isPresent;
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
