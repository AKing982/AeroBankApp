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
        return null;
    }
}
