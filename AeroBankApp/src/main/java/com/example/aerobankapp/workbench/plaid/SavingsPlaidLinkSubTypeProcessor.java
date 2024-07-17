package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;

import java.util.List;

public class SavingsPlaidLinkSubTypeProcessor implements PlaidLinkedAccountProcessor
{

    @Override
    public List<LinkedAccountInfo> process(PlaidAccount plaidAccount, AccountCodeEntity accountCode) {
        return List.of();
    }
}
