package com.example.aerobankapp.services;

import com.example.aerobankapp.DatabaseInitializer;
import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.exceptions.InvalidDatabaseNameException;
import com.example.aerobankapp.repositories.ConnectionRepository;
import com.example.aerobankapp.workbench.runner.DatabaseRunner;
import com.example.aerobankapp.workbench.utilities.ConnectionRequest;
import com.example.aerobankapp.workbench.utilities.DataSourceProperties;
import com.example.aerobankapp.workbench.utilities.connections.ConnectionBuilder;
import com.example.aerobankapp.workbench.utilities.connections.ConnectionBuilderImpl;
import com.example.aerobankapp.workbench.utilities.connections.ConnectionModel;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
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
    private final DatabaseRunner databaseRunner;
    private final DatabaseInitializer databaseInitializer;
    private final Logger LOGGER = LoggerFactory.getLogger(ConnectionsServiceImpl.class);

    @Autowired
    public ConnectionsServiceImpl(DataSourceProperties dataSource,
                                  DatabaseRunner databaseRunner,
                                  DatabaseInitializer databaseInitializer,
                                  ConnectionRepository connectionRepository,
                                  EntityManager entityManager)
    {
        this.connectionRepository = connectionRepository;
        this.databaseRunner = databaseRunner;
        this.databaseInitializer = databaseInitializer;
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
        return connectionRepository.getServerAddressById(id);
    }

    @Override
    public int getPortById(Long id) {
        return connectionRepository.getPortById(id);
    }

    @Override
    public String getUserNameById(Long id) {
        return connectionRepository.getUserNameById(id);
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
    public boolean testConnection(ConnectionRequest connectionRequest)
    {
            DataSource dataSource = createDataSource(connectionRequest);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
    }

    @Override
    public boolean databaseNameExists(ConnectionRequest request) {
        if(request.getDbName() == null || request.getDbName().isEmpty())
        {
            throw new InvalidDatabaseNameException("Invalid Database Name!");
        }
        LOGGER.info("Database Name From CService: " + request.getDbName());
        // Create the dataSource from the request

        // Get the JDBCTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        return databaseRunner.validateDatabaseNameExists(request.getDbName().trim());
    }

    @Override
    public void createDatabase(ConnectionsEntity connectionsEntity)
    {
        // Create the Connection Model
        ConnectionModel connectionModel = createConnectionModel(connectionsEntity);

        // Set the connection model in the database runner
        databaseRunner.setConnectionModel(connectionModel);

        // Verify we can connect to the database
        databaseRunner.setupAndInitializeDatabase(connectionModel.getDbServer(), connectionModel.getDbPort(), connectionModel.getDbName(), connectionModel.getDbUser(), connectionModel.getDbPass(), connectionModel.getDatabaseType());
    }

    private ConnectionModel createConnectionModel(ConnectionsEntity connectionsEntity)
    {
        ConnectionModel connection = new ConnectionModel();
        connection.setDbServer(connectionsEntity.getDbServer());
        connection.setDbPort(connectionsEntity.getDbPort());
        connection.setDbName(connectionsEntity.getDbName());
        connection.setDbUser(connectionsEntity.getDbUser());
        connection.setDbPass(connectionsEntity.getDbPass());
        connection.setDriver("com.mysql.cj.jdbc.Driver");
        connection.setDbProtocol("jdbc:mysql");
        connection.setDatabaseType(connectionsEntity.getDbType());
        return connection;
    }


    private DataSource createDataSource(ConnectionRequest connectionRequest)
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        ConnectionBuilder connectionBuilder = new ConnectionBuilderImpl(connectionRequest);

        dataSource.setDriverClassName(connectionBuilder.getDriverClassName());
        dataSource.setPassword(connectionBuilder.getPassword());
        dataSource.setUsername(connectionBuilder.getUserName());
        dataSource.setUrl(connectionBuilder.getURL());
        return dataSource;
    }
}
