package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AccountService extends ServiceDAOModel<AccountEntity>
{
    @Override
    List<AccountEntity> findAll();

    @Override
    void save(AccountEntity obj);

    @Override
    void delete(AccountEntity obj);

    @Override
    Optional<AccountEntity> findAllById(Long id);

    @Override
    List<AccountEntity> findByUserName(String user);

    BigDecimal getBalanceByAcctID(String acctID);

    BigDecimal getBalanceByAccountCodeUserID(String acctCode, int userID);

    BigDecimal getTotalAccountBalances(String user);

    Long getNumberOfAccounts(String user);

    List<String> getListOfAccountCodes(String user);

    int getAccountIDByAcctCodeAndUserID(int userID, String acctCode);

    Map<Integer, String> getAccountTypeMapByAccountId(String userName);

    void updateAccountBalanceByAcctID(BigDecimal balance, int accountID);
}
