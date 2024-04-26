package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.repositories.AccountPropertiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountPropertiesServiceImpl implements AccountPropertiesService
{
    private final AccountPropertiesRepository accountPropertiesRepository;

    @Autowired
    public AccountPropertiesServiceImpl(AccountPropertiesRepository accountPropertiesRepository){
        this.accountPropertiesRepository = accountPropertiesRepository;
    }

    @Override
    public List<AccountPropertiesEntity> findAll() {
        return null;
    }

    @Override
    public void save(AccountPropertiesEntity obj) {

    }

    @Override
    public void delete(AccountPropertiesEntity obj) {

    }

    @Override
    public Optional<AccountPropertiesEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<AccountPropertiesEntity> findByUserName(String user) {

        List<AccountPropertiesEntity> accountPropertiesEntities = accountPropertiesRepository.findAccountPropertiesByUserName(user);
        return accountPropertiesEntities;
    }

    @Override
    public List<AccountPropertiesEntity> getAccountPropertiesByAcctID(int acctID) {
        return null;
    }

    @Override
    public void saveAll(List<AccountPropertiesEntity> accountPropertiesEntities) {
        accountPropertiesRepository.saveAll(accountPropertiesEntities);
    }
}
