package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.repositories.ConnectionRepository;
import com.example.aerobankapp.workbench.utilities.DataSourceProperties;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
public class ConnectionsServiceImpl implements ConnectionsService
{
    private final ConnectionRepository connectionRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final DataSourceProperties dataSourceProperties;
    private final Logger LOGGER = LoggerFactory.getLogger(ConnectionsServiceImpl.class);

    @Autowired
    public ConnectionsServiceImpl(DataSourceProperties dataSource,
                                  ConnectionRepository connectionRepository,
                                  EntityManager entityManager)
    {
        this.connectionRepository = connectionRepository;
        this.entityManager = entityManager;;
        this.dataSourceProperties = dataSource;
    }

    @Override
    public List<ConnectionsEntity> findAll() {
        return connectionRepository.findAll();
    }

    @Override
    public ConnectionsEntity getConnectionById(Long id)
    {
        return connectionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public void saveConnection(ConnectionsEntity connectionsEntity)
    {
        connectionRepository.save(connectionsEntity);
    }

    @Override
    public void deleteConnection(ConnectionsEntity connectionsEntity) {
        connectionRepository.delete(connectionsEntity);
    }

    @Override
    public String getServerAddressById(Long id) {
        TypedQuery<ConnectionsEntity> connectionsEntityTypedQuery = getEntityManager().createQuery("FROM ConnectionsEntity WHERE id=:id", ConnectionsEntity.class);
        connectionsEntityTypedQuery.setParameter("id", id);
        connectionsEntityTypedQuery.setMaxResults(2);
        ConnectionsEntity connectionsEntity = connectionsEntityTypedQuery.getSingleResult();
        return connectionsEntity.getDbServer();
    }

    @Override
    public int getPortById(Long id) {
        TypedQuery<ConnectionsEntity> connectionsEntityTypedQuery = getEntityManager().createQuery("FROM ConnectionsEntity WHERE id=:id", ConnectionsEntity.class);
        connectionsEntityTypedQuery.setParameter("id", id);
        connectionsEntityTypedQuery.setMaxResults(2);
        ConnectionsEntity connectionsEntity = connectionsEntityTypedQuery.getSingleResult();
        return connectionsEntity.getDbPort();
    }

    @Override
    public String getUserNameById(Long id) {
        TypedQuery<ConnectionsEntity> connectionsEntityTypedQuery = getEntityManager().createQuery("FROM ConnectionsEntity WHERE id=:id", ConnectionsEntity.class);
        connectionsEntityTypedQuery.setParameter("id", id);
        connectionsEntityTypedQuery.setMaxResults(2);
        ConnectionsEntity connectionsEntity = connectionsEntityTypedQuery.getSingleResult();
        return connectionsEntity.getDbUser();
    }

    @Override
    public void connectToDB(ConnectionsEntity connectionsEntity) {
        String url = connectionsEntity.getDbURL();

        try
        {
            dataSourceProperties.setUrl(url);
            dataSourceProperties.setUsername(connectionsEntity.getDbUser());
            dataSourceProperties.setPassword(connectionsEntity.getDbPass());
            dataSourceProperties.setDriverClassName(connectionsEntity.getDbDriver());

        }catch(Exception e)
        {
            LOGGER.error("An exception has occurred: ", e);
        }

    }

    @Override
    public void testConnection(ConnectionsEntity connectionsEntity) {

    }
}
