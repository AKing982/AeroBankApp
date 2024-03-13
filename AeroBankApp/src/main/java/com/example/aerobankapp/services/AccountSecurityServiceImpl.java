package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.repositories.AccountSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountSecurityServiceImpl implements AccountSecurityService
{
    private final AccountSecurityRepository accountSecurityRepository;

    @Autowired
    public AccountSecurityServiceImpl(AccountSecurityRepository accountSecurityRepository){
        this.accountSecurityRepository = accountSecurityRepository;
    }

    @Override
    public List<AccountSecurityEntity> findAll() {
        return null;
    }

    @Override
    public void save(AccountSecurityEntity obj) {

    }

    @Override
    public void delete(AccountSecurityEntity obj) {

    }

    @Override
    public Optional<AccountSecurityEntity> findAllById(Long id) {
        return Optional.empty();
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
