package com.example.aerobankapp.model;

import com.example.aerobankapp.entity.ConnectionsEntity;

public interface ConnectionModel
{
    ConnectionsEntity getConnectionById(Long id);

    void saveConnection(ConnectionsEntity connectionsEntity);

    void deleteConnection(ConnectionsEntity connectionsEntity);

    String getServerAddressById(Long id);
    int getPortById(Long id);
    String getUserNameById(Long id);
    void connectToDB(ConnectionsEntity connectionsEntity);
    void testConnection(ConnectionsEntity connectionsEntity);
}
