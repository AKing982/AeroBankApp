package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.PlaidImportResult;

import java.util.List;

public interface PlaidAccountImporter
{
    List<LinkedAccountInfo> prepareLinkedAccountInfoList(final UserEntity user, final List<PlaidAccount> plaidAccounts);

    LinkedAccountInfo linkAccounts(final PlaidAccount account, final AccountEntity accountEntity);

    PlaidImportResult importDataFromPlaidAccountToSystemAccount(final PlaidAccount plaidAccount, final Account account);

    Boolean validateAccountSubTypeToTypeCriteria(final List<AccountEntity> accountEntities);

    Boolean executeCreateAndSaveExternalAccountEntity(List<LinkedAccountInfo> accountIdsMap);
}
