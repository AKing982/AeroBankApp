package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountCodeDTO;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.repositories.AccountCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountCodeServiceImpl implements AccountCodeService
{

    private final AccountCodeRepository accountCodeRepository;

    @Autowired
    public AccountCodeServiceImpl(AccountCodeRepository accountCodeRepository){
        this.accountCodeRepository = accountCodeRepository;
    }

    @Override
    public List<AccountCodeEntity> findAll() {
        return null;
    }

    @Override
    public void save(AccountCodeEntity obj) {

    }

    @Override
    public void delete(AccountCodeEntity obj) {

    }

    @Override
    public Optional<AccountCodeEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<AccountCodeEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public AccountCodeEntity buildAccountCodeEntity(AccountCodeDTO accountCodeDTO) {
        return null;
    }
}
