package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountDetailsEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountDetailsServiceImpl implements AccountDetailsService
{

    @Override
    public List<AccountDetailsEntity> findAll() {
        return null;
    }

    @Override
    public void save(AccountDetailsEntity obj) {

    }

    @Override
    public void delete(AccountDetailsEntity obj) {

    }

    @Override
    public Optional<AccountDetailsEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<AccountDetailsEntity> findByUserName(String user) {
        return null;
    }
}
