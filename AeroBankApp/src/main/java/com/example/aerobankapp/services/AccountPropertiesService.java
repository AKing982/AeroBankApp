package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;

public interface AccountPropertiesService extends ServiceDAOModel<AccountPropertiesEntity>
{
    List<AccountPropertiesEntity> getAccountPropertiesByAcctID(int acctID);
}
