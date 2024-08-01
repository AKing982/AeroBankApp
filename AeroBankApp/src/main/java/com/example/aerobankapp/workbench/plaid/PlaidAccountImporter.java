package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.PlaidImportResult;

import java.util.List;
import java.util.Map;

public interface PlaidAccountImporter
{
    List<LinkedAccountInfo> prepareLinkedAccounts(final UserEntity user, final List<PlaidAccount> plaidAccounts);

    LinkedAccountInfo linkAccounts(final PlaidAccount account, final AccountEntity accountEntity);

    PlaidImportResult importDataFromPlaidAccountToSystemAccount(final PlaidAccount plaidAccount, final Account account);

    Boolean validateAccountSubTypeToTypeCriteria(final List<AccountEntity> accountEntities);

    Boolean executeCreateAndSaveExternalAccountEntity(List<LinkedAccountInfo> accountIdsMap);

    void createImportedAccountsFromNonLinkAccountsList(Map<Integer, List<PlaidAccount>> unlinkedPlaidAccounts);

    Map<Integer, List<PlaidAccount>> getPlaidAccountsMap();
    Map<String, Integer> getSingleSysAndPlaidAcctIdMap(final ExternalAccountsEntity externalAccountsEntity);

}
