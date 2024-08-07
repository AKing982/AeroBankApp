package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.PlaidLinkEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.PlaidLinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaidLinkServiceImpl implements PlaidLinkService
{
    private final PlaidLinkRepository plaidLinkRepository;
    private Logger LOGGER = LoggerFactory.getLogger(PlaidLinkServiceImpl.class);

    @Autowired
    public PlaidLinkServiceImpl(PlaidLinkRepository plaidLinkRepository)
    {
        this.plaidLinkRepository = plaidLinkRepository;
    }

    public PlaidLinkEntity buildPlaidLinkEntity(String access_token, String item_id, int userID, String institutionName)
    {
        return PlaidLinkEntity.builder()
                .accessToken(access_token)
                .item_id(item_id)
                .user(UserEntity.builder().userID(userID).build())
                .institution_name(institutionName)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Boolean hasPlaidLink(int userID) {
        if(userID < 1)
        {
            throw new IllegalArgumentException("Invalid UserID found: " + userID);
        }
        return plaidLinkRepository.existsByUserId(userID);
    }

    @Override
    public Optional<PlaidLinkEntity> findPlaidLinkEntityByUserId(int userID) {
        return plaidLinkRepository.findByUserId(userID);
    }

    @Override
    public List<PlaidLinkEntity> findAll() {
        return List.of();
    }

    @Override
    public void save(PlaidLinkEntity obj)
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

        plaidLinkRepository.save(obj);
    }

    private boolean validatePlaidAccountEntityCriteria(PlaidLinkEntity criteria)
    {
        return criteria.getAccessToken() != null && criteria.getUser() != null && criteria.getInstitution_name() != null
                && criteria.getItem_id() != null && criteria.getCreatedAt() != null && criteria.getUpdatedAt() != null;
    }

    @Override
    public void delete(PlaidLinkEntity obj) {

    }

    @Override
    public Optional<PlaidLinkEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<PlaidLinkEntity> findByUserName(String user) {
        return List.of();
    }
}
