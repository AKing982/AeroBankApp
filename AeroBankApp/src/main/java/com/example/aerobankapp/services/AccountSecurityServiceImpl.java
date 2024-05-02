package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.repositories.AccountSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.aerobankapp.services.utilities.AccountSecurityServiceUtil.buildSecurityEntity;

@Service
public class AccountSecurityServiceImpl implements AccountSecurityService
{
    private final AccountSecurityRepository accountSecurityRepository;
    private final AccountService accountService;

    @Autowired
    public AccountSecurityServiceImpl(AccountSecurityRepository accountSecurityRepository,
                                      AccountService accountService){
        this.accountSecurityRepository = accountSecurityRepository;
        this.accountService = accountService;
    }

    @Override
    public List<AccountSecurityEntity> findAll() {
        return null;
    }

    @Override
    public void save(AccountSecurityEntity obj) {
        accountSecurityRepository.save(obj);
    }

    @Override
    public void saveAll(List<AccountSecurityEntity> accountSecurityEntities) {
        accountSecurityRepository.saveAll(accountSecurityEntities);
    }

    @Override
    public void delete(AccountSecurityEntity obj) {
        accountSecurityRepository.delete(obj);
    }

    @Override
    @Transactional
    public void deleteAll(List<AccountSecurityEntity> accountSecurityEntities) {
        accountSecurityRepository.deleteAll(accountSecurityEntities);
    }

    @Override
    public Optional<AccountSecurityEntity> findAllById(Long id) {
        return Optional.empty();
    }


    @Override
    public List<AccountSecurityEntity> getAccountSecurityEntityListFromAccounts(List<AccountEntity> accountEntities){
        List<AccountSecurityEntity> accountSecurityEntities = new ArrayList<>();
        for(AccountEntity account : accountEntities){
            if(account != null){
                AccountSecurityEntity accountSecurityEntity = buildSecurityEntity(account);
                accountSecurityEntities.add(accountSecurityEntity);
            }
        }
        return accountSecurityEntities;
    }



    @Override
    public List<AccountSecurityEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public BigDecimal getMinimumBalanceRequirementsByAcctID(int acctID) {
        return accountSecurityRepository.getMinimumBalanceRequirementsByAcctID(acctID);
    }

    @Override
    public List<AccountSecurityEntity> getAccountSecurityListByUserID(int userID) {
        return accountSecurityRepository.findAccountSecurityListByUserID(userID);
    }

    @Override
    public int getTransactionVelocityLimitByAcctID(int acctID) {
        return 0;
    }

    @Override
    public boolean IsWithdrawEnabledByAcctID(int acctID) {
        return false;
    }

    @Override
    public boolean IsDepositEnabledByAcctID(int acctID) {
        return false;
    }

    @Override
    public boolean IsTransferEnabledByAcctID(int acctID) {
        return false;
    }

    @Override
    public boolean IsPurchaseEnabledByAcctID(int acctID) {
        return false;
    }

    @Override
    public boolean IsAccountLockedByAcctID(int acctID) {
        return false;
    }

    @Override
    public boolean IsAutoPayEnabledByAcctID(int acctID) {
        return false;
    }

    @Override
    public boolean AreFeesEnabledByAcctID(int acctID) {
        return false;
    }


    @Override
    public boolean InterestEnabledByAcctID(int acctID) {
        return false;
    }

    @Override
    public int getTransactionVelocityLimitByID(int ID) {
        return 0;
    }

    @Override
    public boolean IsWithdrawEnabledByID(int ID) {
        return false;
    }

    @Override
    public boolean IsDepositEnabledByID(int ID) {
        return false;
    }

    @Override
    public boolean IsTransferEnabledByID(int ID) {
        return false;
    }

    @Override
    public boolean IsPurchaseEnabledByID(int ID) {
        return false;
    }

    @Override
    public boolean IsAccountLockedByID(int ID) {
        return false;
    }

    @Override
    public boolean IsAutoPayEnabledByID(int ID) {
        return false;
    }

    @Override
    public boolean AreFeesEnabledByID(int ID) {
        return false;
    }


    @Override
    public boolean IsInterestEnabledByID(int ID) {
        return false;
    }


    @Override
    public void updateAccountSecurity(AccountSecurityEntity accountSecurityEntity) {

    }
}
