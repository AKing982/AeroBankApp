package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.repositories.ExternalAccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalAccountsServiceImpl implements ExternalAccountsService
{
    private ExternalAccountsRepository externalAccountsRepository;

    @Autowired
    public ExternalAccountsServiceImpl(ExternalAccountsRepository externalAccountsRepository)
    {
        this.externalAccountsRepository = externalAccountsRepository;
    }

    @Override
    public List<ExternalAccountsEntity> findAll() {
        return List.of();
    }

    @Override
    @Transactional
    public void save(ExternalAccountsEntity obj) {
        externalAccountsRepository.save(obj);
    }

    @Override
    @Transactional
    public void delete(ExternalAccountsEntity obj) {
        externalAccountsRepository.delete(obj);
    }

    @Override
    public Optional<ExternalAccountsEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<ExternalAccountsEntity> findByUserName(String user) {
        return List.of();
    }

    @Override
    public ExternalAccountsEntity createExternalAccount(String externalAcctID, int acctID) {
        ExternalAccountsEntity externalAccountsEntity = new ExternalAccountsEntity();
        externalAccountsEntity.setExternalAcctID(externalAcctID);
        externalAccountsEntity.setAccount(AccountEntity.builder().acctID(acctID).build());
        return externalAccountsEntity;
    }

    @Override
    @Transactional
    public Optional<ExternalAccountsEntity> getExternalAccount(String externalAcctID) {
        return externalAccountsRepository.findByExternalAcctID(externalAcctID);
    }
}
