package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.PlaidAccount;

import java.util.List;
import java.util.Map;

public interface PlaidAccountToSystemAccountMapper
{
    Map<Integer, List<String>> getUserToAccountIdsMap(UserEntity user, List<PlaidAccount> plaidAccounts);
}
