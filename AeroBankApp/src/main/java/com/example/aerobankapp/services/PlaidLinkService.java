package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.PlaidLinkEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.Optional;

public interface PlaidLinkService extends ServiceDAOModel<PlaidLinkEntity>
{
    PlaidLinkEntity buildPlaidLinkEntity(String access_token, String item_id, int userID, String institutionName);

    Boolean hasPlaidLink(int userID);

    Optional<PlaidLinkEntity> findPlaidLinkEntityByUserId(int userID);
}
