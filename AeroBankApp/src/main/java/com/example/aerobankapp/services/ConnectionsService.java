package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.model.ConnectionModel;

public interface ConnectionsService extends ConnectionModel
{
    @Override
    ConnectionsEntity getConnectionById(Long id);

    @Override
    void saveConnection(ConnectionsEntity connectionsEntity);

    @Override
    void deleteConnection(ConnectionsEntity connectionsEntity);

    @Override
    String getServerAddressById(Long id);

    @Override
    int getPortById(Long id);

    @Override
    String getUserNameById(Long id);
}
