package com.example.aerobankapp.controllers.utils;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.workbench.utilities.response.AccountCodeResponse;
import com.example.aerobankapp.workbench.utilities.response.AccountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountControllerUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(AccountControllerUtil.class);

    public static List<AccountCodeResponse> getAccountCodesAsResponse(List<String> accountCodes)
    {
        List<AccountCodeResponse> accountCodeResponseList = new ArrayList<>();
        for(String code : accountCodes)
        {
            AccountCodeResponse accountCodeResponse = new AccountCodeResponse(code);
            accountCodeResponseList.add(accountCodeResponse);
        }
        return accountCodeResponseList;
    }


    public static List<AccountResponse> getAccountAsResponse(List<String> accountCodes) {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for (String code : accountCodes) {
            AccountResponse accountResponse = new AccountResponse(code);
            accountResponseList.add(accountResponse);
        }

        return accountResponseList;
    }

    public static List<AccountCodeResponse> getAccountCodeResponseList(List<AccountEntity> accountEntities)
    {
        List<AccountCodeResponse> accountCodeResponses = new ArrayList<>();
        for(AccountEntity accountEntity : accountEntities)
        {
            AccountCodeResponse accountCodeResponse = new AccountCodeResponse(accountEntity.getAccountCode());
            accountCodeResponses.add(accountCodeResponse);
        }
        return accountCodeResponses;
    }

    public static List<AccountResponse> getAccountResponseList(List<AccountPropertiesEntity> accountProperties, List<AccountEntity> entityList, BigDecimal pending, BigDecimal available)
    {
        List<AccountResponse> accountResponseList = new ArrayList<>();

        // Map for quick lookup of AccountProperties by AccountEntity ID
        Map<Integer, AccountPropertiesEntity> propertiesMap = new HashMap<>();
        for (AccountPropertiesEntity prop : accountProperties) {
            if (prop.getAccount() != null) {
                propertiesMap.put(prop.getAccount().getAcctID(), prop);
            }
        }

        for (AccountEntity entity : entityList) {
            BigDecimal balance = entity.getBalance();
            String acctCode = entity.getAccountCode();
            String accountName = entity.getAccountName();

            // Lookup account properties using AccountEntity ID
            AccountPropertiesEntity prop = propertiesMap.get(entity.getAcctID());
            String acctColor = prop != null ? prop.getAcct_color() : null;
            String acctImage = prop != null ? prop.getImage_url() : null; // Use image_url as acctImage
            LOGGER.info("Account Color: " + acctColor);
            LOGGER.info("Account Image: " + acctImage);
            AccountResponse accountResponse = new AccountResponse(
                    acctCode, balance, pending, available, accountName, acctColor, acctImage);
            accountResponseList.add(accountResponse);
        }

        return accountResponseList;
    }
}
