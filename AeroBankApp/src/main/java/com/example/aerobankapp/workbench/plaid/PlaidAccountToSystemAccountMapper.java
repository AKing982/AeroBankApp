package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;

import java.util.List;
import java.util.Map;

public interface PlaidAccountToSystemAccountMapper
{
    List<LinkedAccountInfo> getLinkedAccountInfoList(UserEntity user, List<PlaidAccount> plaidAccounts);
    Boolean executeCreateAndSaveExternalAccountEntity(List<LinkedAccountInfo> accountIdsMap);
}
