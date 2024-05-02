package com.example.aerobankapp.services;

import com.example.aerobankapp.embeddables.AccountUserEmbeddable;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountUserEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.AccountUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountUsersEntityServiceImpl implements AccountUsersEntityService
{
    private AccountUsersRepository accountUsersRepository;

    @Autowired
    public AccountUsersEntityServiceImpl(AccountUsersRepository accountUsersRepository){
        this.accountUsersRepository = accountUsersRepository;
    }

    @Override
    public List<AccountUserEntity> findAll() {
        return null;
    }

    @Override
    public void save(AccountUserEntity obj) {
        accountUsersRepository.save(obj);
    }

    @Override
    @Transactional
    public void delete(AccountUserEntity obj) {
        accountUsersRepository.delete(obj);
    }

    @Override
    public Optional<AccountUserEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<AccountUserEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public void saveAll(List<AccountUserEntity> accountUserEntityList) {
        accountUsersRepository.saveAll(accountUserEntityList);
    }

    @Override
    public AccountUserEntity buildAccountUserEntity(AccountEntity accountEntity, UserEntity user) {
        AccountUserEmbeddable accountUserEmbeddable = new AccountUserEmbeddable();
        accountUserEmbeddable.setUserID(user.getUserID());
        accountUserEmbeddable.setAcctID(accountEntity.getAcctID());

        AccountUserEntity accountUserEntity = new AccountUserEntity();
        accountUserEntity.setAccount(accountEntity);
        accountUserEntity.setUser(user);
        accountUserEntity.setId(accountUserEmbeddable);
        return accountUserEntity;
    }

    @Override
    public List<AccountUserEntity> getAccountUserEntityListByUserID(int userID) {
        return accountUsersRepository.findAccountUserEntitiesByUserID(userID);
    }

    @Override
    public List<AccountUserEntity> getAccountUserEntityList(List<AccountEntity> accountEntities, UserEntity user) {
        List<AccountUserEntity> accountUserEntityList = new ArrayList<>();
        for(AccountEntity accountEntity : accountEntities){
            if(accountEntity != null){
                AccountUserEntity accountUserEntity = buildAccountUserEntity(accountEntity, user);
                accountUserEntityList.add(accountUserEntity);
            }
        }
        return accountUserEntityList;
    }

    @Override
    public void deleteAll(List<AccountUserEntity> accountUserEntities) {
        accountUsersRepository.deleteAll(accountUserEntities);
    }
}
