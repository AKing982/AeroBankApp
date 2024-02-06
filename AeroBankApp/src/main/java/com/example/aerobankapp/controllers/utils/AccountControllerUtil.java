package com.example.aerobankapp.controllers.utils;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.workbench.utilities.response.AccountCodeResponse;
import com.example.aerobankapp.workbench.utilities.response.AccountResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountControllerUtil {


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

    public static List<AccountResponse> getAccountResponseList(List<AccountEntity> entityList, BigDecimal pending, BigDecimal available)
    {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(AccountEntity entity : entityList)
        {
            BigDecimal balance = entity.getBalance();
            String acctCode = entity.getAccountCode();
            String acctColor = entity.getAcct_color();
            AccountResponse accountResponse = new AccountResponse(acctCode, balance, pending, available, acctColor);
            accountResponseList.add(accountResponse);
        }

        return accountResponseList;
    }
}
