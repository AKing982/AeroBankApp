package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.repositories.AccountPropertiesRepository;
import com.example.aerobankapp.workbench.utilities.color.AccountPropertiesSelector;
import com.example.aerobankapp.workbench.utilities.color.AccountPropertiesSelectorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountPropertiesServiceImpl implements AccountPropertiesService
{
    private final AccountPropertiesRepository accountPropertiesRepository;
    private final AccountPropertiesSelector accountPropertiesSelector;

    @Autowired
    public AccountPropertiesServiceImpl(AccountPropertiesRepository accountPropertiesRepository, AccountPropertiesSelector accountPropertiesSelector){
        this.accountPropertiesRepository = accountPropertiesRepository;
        this.accountPropertiesSelector = accountPropertiesSelector;
    }

    @Override
    public List<AccountPropertiesEntity> findAll() {
        return null;
    }

    @Override
    public void save(AccountPropertiesEntity obj) {
        accountPropertiesRepository.save(obj);
    }

    @Override
    public void delete(AccountPropertiesEntity obj) {
        accountPropertiesRepository.delete(obj);
    }

    @Override
    public Optional<AccountPropertiesEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<AccountPropertiesEntity> findByUserName(String user) {

        return accountPropertiesRepository.findAccountPropertiesByUserName(user);
    }

    @Override
    public List<AccountPropertiesEntity> getAccountPropertiesByAcctID(int acctID) {
        return null;
    }

    @Override
    public List<AccountPropertiesEntity> getAccountPropertiesByUserID(int userID) {
        return accountPropertiesRepository.findAccountPropertiesEntitiesByUserID(userID);
    }

    @Override
    public void saveAll(List<AccountPropertiesEntity> accountPropertiesEntities) {
        accountPropertiesRepository.saveAll(accountPropertiesEntities);
    }

    @Override
    public void deleteAll(List<AccountPropertiesEntity> accountPropertiesEntities) {
        accountPropertiesRepository.deleteAll(accountPropertiesEntities);
    }

    @Override
    public AccountPropertiesEntity buildAccountPropertiesEntity(AccountEntity accountEntity) {

        // Randomly select an acct_color from a list
        String randomAcctColor = accountPropertiesSelector.selectRandomAccountColor();

        // Randomly select an image_url from a list
        String randomImage = accountPropertiesSelector.selectRandomImageURL();

        // Build the Entity
         return buildEntity(accountEntity, randomAcctColor, randomImage);
    }

    public List<AccountPropertiesEntity> getListOfAccountPropertiesFromAccountEntity(List<AccountEntity> accountEntities){
        List<AccountPropertiesEntity> accountPropertiesEntities = new ArrayList<>();
        for(AccountEntity accountEntity : accountEntities){
            if(accountEntity != null){
                AccountPropertiesEntity accountPropertiesEntity = buildAccountPropertiesEntity(accountEntity);
                accountPropertiesEntities.add(accountPropertiesEntity);
            }
        }
        return accountPropertiesEntities;
    }

    private AccountPropertiesEntity buildEntity(AccountEntity accountEntity, String acctColor, String imageUrl){
        AccountPropertiesEntity accountPropertiesEntity = new AccountPropertiesEntity();
        accountPropertiesEntity.setAccount(accountEntity);
        accountPropertiesEntity.setAcct_color(acctColor);
        accountPropertiesEntity.setImage_url(imageUrl);
        return accountPropertiesEntity;
    }
}
