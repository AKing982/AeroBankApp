package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;

import java.util.List;

public interface PlaidLinkedAccountProcessor
{
    List<LinkedAccountInfo> process(final PlaidAccount plaidAccount, AccountCodeEntity accountCode);
}
