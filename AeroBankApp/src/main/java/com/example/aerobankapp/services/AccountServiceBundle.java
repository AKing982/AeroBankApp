package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@Builder
@Getter
@Setter
public class AccountServiceBundle
{
    private final AccountDAOImpl accountDAO;

    @Autowired
    public AccountServiceBundle(AccountDAOImpl accountDAO)
    {
        this.accountDAO = accountDAO;
    }

    public List<AccountEntity> getAccountsByUserName(String user)
    {
        return accountDAO.findByUserName(user);
    }

    public List<AccountEntity> getAllAccounts()
    {
        return accountDAO.findAll();
    }

    public void saveAccount(AccountEntity accountEntity)
    {
        accountDAO.save(accountEntity);
    }

    public void deleteAccount(AccountEntity accountEntity)
    {
        accountDAO.delete(accountEntity);
    }

    public Optional<AccountEntity> getAccountById(int id)
    {
        Long longId = (long) id;
        return accountDAO.findAllById(longId);
    }
}
