package com.example.aerobankapp.manager;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.account.AccountCodeDTO;
import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.BalanceHistoryDTO;
import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.AccountEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface AccountManager
{
    Set<AccountDTO> getAccounts(int userID);
    Set<UserDTO> getUsersLinkedToAccounts(List<Integer> userIDs);
    void deleteAccount(AccountEntity account);
    void updateAccount(AccountEntity account);
    BigDecimal getBalanceFromAccount(String acctID);
    BigDecimal getBalanceByAccountCode(AccountCodeDTO code);
}
