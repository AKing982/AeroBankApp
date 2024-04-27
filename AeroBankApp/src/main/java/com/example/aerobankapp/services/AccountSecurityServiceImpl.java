package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.repositories.AccountSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<AccountSecurityEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public AccountSecurityEntity buildSecurityEntity(AccountEntity account) {
        AccountSecurityEntity accountSecurityEntity = new AccountSecurityEntity();
        AccountType accountType = AccountType.getInstance(account.getAccountType());
        return buildAccountSecurityEntityProperties(account);
    }

    public AccountSecurityEntity buildAccountSecurityEntityProperties(AccountEntity account){
        AccountType accountType = AccountType.getInstance(account.getAccountType());
        AccountSecurityEntity accountSecurity = new AccountSecurityEntity();

        // Convert the Account object to an account Entity
       // AccountEntity accountEntity = accountService.buildAccountEntityByAccountModel(account);
        switch(accountType){
            case CHECKING -> {
                accountSecurity = buildEntity(true,
                        false,
                        1000,
                        1000,
                        25,
                        true,
                        true,
                        true,
                        true,
                        false,
                        10,
                        new BigDecimal("120.000"),
                        true, account);
            }
            case SAVINGS -> {
                accountSecurity = buildEntity(true,
                        false,
                        1000,
                        20,
                        15,
                        true,
                        true,
                        true,
                        true,
                        true,
                        5,
                        new BigDecimal("300.000"),
                        true, account);
            }
            case RENT -> {
                accountSecurity = buildEntity(true,
                        false,
                        1000,
                        90,
                        100,
                        true,
                        true,
                        true,
                        true,
                        true,
                        11,
                        new BigDecimal("1215.000"),
                        true,
                        account);
            }
            case INVESTMENT -> {
                accountSecurity = buildEntity(true,
                        false,
                        1000,
                        12,
                        10,
                        true,
                        true,
                        true,
                        false,
                        false,
                        2,
                        new BigDecimal("8500.000"),
                        false, account);
            }
        }
        return accountSecurity;
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

    private AccountSecurityEntity buildEntity(boolean isEnabled, boolean isAccountLocked, int depositLimit, int withdrawLimit, int transferLimit, boolean depositEnabled, boolean withdrawEnabled, boolean transferEnabled,
                                              boolean interestEnabled, boolean feesEnabled, int transferVelocityLimit, BigDecimal minimumBalance, boolean autoPayOn, AccountEntity account){
        AccountSecurityEntity accountSecurity = new AccountSecurityEntity();
        accountSecurity.setEnabled(isEnabled);
        accountSecurity.setAccountLocked(isAccountLocked);
        accountSecurity.setDepositLimit(depositLimit);
        accountSecurity.setWithdrawLimit(withdrawLimit);
        accountSecurity.setDepositEnabled(depositEnabled);
        accountSecurity.setFeesEnabled(feesEnabled);
        accountSecurity.setInterestEnabled(interestEnabled);
        accountSecurity.setMinimumBalance(minimumBalance);
        accountSecurity.setTransferLimit(transferLimit);
        accountSecurity.setAutoPayEnabled(autoPayOn);
        accountSecurity.setTransactionVelocityLimit(transferVelocityLimit);
        accountSecurity.setTransferEnabled(transferEnabled);
        accountSecurity.setWithdrawEnabled(withdrawEnabled);
        accountSecurity.setAccount(account);
        return accountSecurity;
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
