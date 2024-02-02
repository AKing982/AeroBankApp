package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.repositories.ConnectionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionsServiceImpl implements ConnectionsService
{
    private final ConnectionRepository connectionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ConnectionsServiceImpl(ConnectionRepository connectionRepository, EntityManager entityManager)
    {
        this.connectionRepository = connectionRepository;
        this.entityManager = entityManager;
    }


    @Override
    public List<ConnectionsEntity> findAll() {
        return connectionRepository.findAll();
    }

    @Override
    public ConnectionsEntity getConnectionById(Long id)
    {
        return null;
    }

    @Override
    public void saveConnection(ConnectionsEntity connectionsEntity) {

    }

    @Override
    public void deleteConnection(ConnectionsEntity connectionsEntity) {

    }

    @Override
    public String getServerAddressById(Long id) {
        return null;
    }

    @Override
    public int getPortById(Long id) {
        return 0;
    }

    @Override
    public String getUserNameById(Long id) {
        return null;
    }

    @Override
    public void connectToDB(ConnectionsEntity connectionsEntity) {

    }

    @Override
    public void testConnection(ConnectionsEntity connectionsEntity) {

    }
}
