package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountSecurityService extends ServiceDAOModel<AccountSecurityEntity>
{
    @Override
    List<AccountSecurityEntity> findAll();

    @Override
    void save(AccountSecurityEntity obj);

    void saveAll(List<AccountSecurityEntity> accountSecurityEntities);

    @Override
    void delete(AccountSecurityEntity obj);

    @Override
    Optional<AccountSecurityEntity> findAllById(Long id);


    @Override
    List<AccountSecurityEntity> findByUserName(String user);

    BigDecimal getMinimumBalanceRequirementsByAcctID(int acctID);

    List<AccountSecurityEntity> getAccountSecurityEntityListFromAccounts(List<AccountEntity> accountEntities);
    int getTransactionVelocityLimitByAcctID(int acctID);
    boolean IsWithdrawEnabledByAcctID(int acctID);
    boolean IsDepositEnabledByAcctID(int acctID);
    boolean IsTransferEnabledByAcctID(int acctID);
    boolean IsPurchaseEnabledByAcctID(int acctID);
    boolean IsAccountLockedByAcctID(int acctID);
    boolean IsAutoPayEnabledByAcctID(int acctID);
    boolean AreFeesEnabledByAcctID(int acctID);
    boolean InterestEnabledByAcctID(int acctID);
    int getTransactionVelocityLimitByID(int ID);
    boolean IsWithdrawEnabledByID(int ID);
    boolean IsDepositEnabledByID(int ID);
    boolean IsTransferEnabledByID(int ID);
    boolean IsPurchaseEnabledByID(int ID);
    boolean IsAccountLockedByID(int ID);
    boolean IsAutoPayEnabledByID(int ID);
    boolean AreFeesEnabledByID(int ID);
    boolean IsInterestEnabledByID(int ID);
    void updateAccountSecurity(AccountSecurityEntity accountSecurityEntity);

}
