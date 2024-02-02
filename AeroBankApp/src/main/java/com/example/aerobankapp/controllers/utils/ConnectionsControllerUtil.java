package com.example.aerobankapp.controllers.utils;

import com.example.aerobankapp.dto.ConnectionsDTO;
import com.example.aerobankapp.entity.ConnectionsEntity;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsControllerUtil
{
    public static List<ConnectionsDTO> getListOfConnectionDTO(List<ConnectionsEntity> connectionsEntities)
    {
        List<ConnectionsDTO> connectionsDTOS = new ArrayList<>();
        for(ConnectionsEntity connectionsEntity : connectionsEntities)
        {
            ConnectionsDTO connectionsDTO = connectionDTOBuilder(connectionsEntity);
            connectionsDTOS.add(connectionsDTO);
        }
        return connectionsDTOS;
    }

    public static ConnectionsDTO connectionDTOBuilder(ConnectionsEntity connectionsEntity)
    {
        return ConnectionsDTO.builder()
                .connectionId(connectionsEntity.getConnectionID())
                .dbName(connectionsEntity.getDbName())
                .dbServer(connectionsEntity.getDbServer())
                .dbPort(connectionsEntity.getDbPort())
                .dbPass(connectionsEntity.getDbPass())
                .dbType(connectionsEntity.getDbType())
                .dbUser(connectionsEntity.getDbUser())
                .dateModified(connectionsEntity.getDateModified())
                .build();
    }
}
