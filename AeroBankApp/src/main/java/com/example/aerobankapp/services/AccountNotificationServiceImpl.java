package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.repositories.AccountNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountNotificationServiceImpl implements AccountNotificationService
{
    private final AccountNotificationRepository accountNotificationRepository;

    @Autowired
    public AccountNotificationServiceImpl(AccountNotificationRepository accountNotificationRepository){
        this.accountNotificationRepository = accountNotificationRepository;
    }

    @Override
    public List<AccountNotificationEntity> findAll() {
        return null;
    }

    @Override
    public void save(AccountNotificationEntity obj) {

    }

    @Override
    public void delete(AccountNotificationEntity obj) {

    }

    @Override
    public Optional<AccountNotificationEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<AccountNotificationEntity> findByUserName(String user) {
        return null;
    }
}
