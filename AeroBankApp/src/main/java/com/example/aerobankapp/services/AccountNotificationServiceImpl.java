package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountNotificationServiceImpl implements AccountNotificationService
{

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
