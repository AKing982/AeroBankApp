package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.PlaidAccountsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaidAccountsServiceImpl implements PlaidAccountsService
{
    private final PlaidAccountsRepository plaidAccountsRepository;
    private Logger LOGGER = LoggerFactory.getLogger(PlaidAccountsServiceImpl.class);

    @Autowired
    public PlaidAccountsServiceImpl(PlaidAccountsRepository plaidAccountsRepository)
    {
        this.plaidAccountsRepository = plaidAccountsRepository;
    }

    public PlaidAccountsEntity buildPlaidAccountsEntity(String access_token, String item_id, int userID, String institutionName)
    {
        return PlaidAccountsEntity.builder()
                .accessToken(access_token)
                .item_id(item_id)
                .user(UserEntity.builder().userID(userID).build())
                .institution_name(institutionName)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Boolean hasPlaidAccount(int userID) {
        if(userID < 1)
        {
            throw new IllegalArgumentException("Invalid UserID found: " + userID);
        }
        return plaidAccountsRepository.existsByUserId(userID);
    }

    @Override
    public Optional<PlaidAccountsEntity> findPlaidAccountEntityByUserId(int userID) {
        return plaidAccountsRepository.findByUserId(userID);
    }

    @Override
    public List<PlaidAccountsEntity> findAll() {
        return List.of();
    }

    @Override
    public void save(PlaidAccountsEntity obj)
    {
        if(obj == null)
        {
            throw new IllegalArgumentException("PlaidAccountsEntity cannot be null");
        }

        if(!validatePlaidAccountEntityCriteria(obj))
        {
            LOGGER.warn("Found Null Plaid Account Criteria: {} ", obj.toString());
            throw new IllegalArgumentException("Found Null Plaid Account Criteria: " + obj.toString());
        }

        plaidAccountsRepository.save(obj);
    }

    private boolean validatePlaidAccountEntityCriteria(PlaidAccountsEntity criteria)
    {
        return criteria.getAccessToken() != null && criteria.getUser() != null && criteria.getInstitution_name() != null
                && criteria.getItem_id() != null && criteria.getCreatedAt() != null && criteria.getUpdatedAt() != null;
    }

    @Override
    public void delete(PlaidAccountsEntity obj) {

    }

    @Override
    public Optional<PlaidAccountsEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<PlaidAccountsEntity> findByUserName(String user) {
        return List.of();
    }
}
