package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import com.example.aerobankapp.exceptions.AccountNotFoundException;
import com.example.aerobankapp.repositories.AccountRepository;
import com.example.aerobankapp.repositories.ExternalAccountsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalAccountsServiceImpl implements ExternalAccountsService
{
    private ExternalAccountsRepository externalAccountsRepository;
    private AccountRepository accountRepository;
    private Logger LOGGER = LoggerFactory.getLogger(ExternalAccountsServiceImpl.class);

    @Autowired
    public ExternalAccountsServiceImpl(ExternalAccountsRepository externalAccountsRepository,
                                       AccountRepository accountRepository)
    {
        this.externalAccountsRepository = externalAccountsRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<ExternalAccountsEntity> findAll() {
        return List.of();
    }

    @Override
    @Transactional
    public void save(ExternalAccountsEntity obj) {
        LOGGER.info("Saving ExternalAccountsEntity: {}", obj);
        try
        {
            externalAccountsRepository.save(obj);

        }catch(Exception e)
        {
            LOGGER.error("There was an error saving the ExternalAccountsEntity: {}", obj, e);
        }

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

        Optional<AccountEntity> accountEntity = Optional.ofNullable(accountRepository.findById(acctID)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + acctID)));

        accountEntity.ifPresent(externalAccountsEntity::setAccount);

        return externalAccountsEntity;
    }

    @Override
    @Transactional
    public Optional<ExternalAccountsEntity> getExternalAccount(String externalAcctID) {
        return externalAccountsRepository.findByExternalAcctID(externalAcctID);
    }
}
