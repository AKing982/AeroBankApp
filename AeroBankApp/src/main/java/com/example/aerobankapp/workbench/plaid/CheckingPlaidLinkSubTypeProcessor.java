package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;

import java.util.ArrayList;
import java.util.List;

public class CheckingPlaidLinkSubTypeProcessor implements PlaidLinkedAccountProcessor
{

    @Override
    public List<LinkedAccountInfo> process(PlaidAccount plaidAccount, AccountCodeEntity accountCode) {
        List<LinkedAccountInfo> linkedAccountInfos = new ArrayList<>();
        String accountType = accountCode.getAccountType();
        if(accountType.equals("01"))
        {

        }
        return List.of();
    }
}
