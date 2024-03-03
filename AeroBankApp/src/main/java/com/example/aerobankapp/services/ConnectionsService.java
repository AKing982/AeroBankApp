package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.model.ConnectionModel;
import com.example.aerobankapp.workbench.utilities.ConnectionRequest;

import java.util.List;

public interface ConnectionsService extends ConnectionModel
{
    List<ConnectionsEntity> findAll();

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

    @Override
    void connectToDB(ConnectionsEntity connectionsEntity);

    @Override
    boolean testConnection(ConnectionRequest connectionRequest);


}
