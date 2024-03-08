package com.example.aerobankapp.controllers;


import com.example.aerobankapp.dto.ConnectionsDTO;
import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.services.ConnectionsService;
import com.example.aerobankapp.services.ConnectionsServiceImpl;
import com.example.aerobankapp.services.DatabaseSchemaService;
import com.example.aerobankapp.services.DatabaseSchemaServiceImpl;
import com.example.aerobankapp.workbench.runner.DatabaseRunner;
import com.example.aerobankapp.workbench.utilities.ConnectionRequest;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.example.aerobankapp.controllers.utils.ConnectionsControllerUtil.getListOfConnectionDTO;

@Controller
@RequestMapping(value="/api/connections", method = RequestMethod.GET)
@CrossOrigin(value="http://localhost:3000")
public class ConnectionsController {

    private final ConnectionsService connectionsService;
    private Logger LOGGER = LoggerFactory.getLogger(ConnectionsController.class);

    @Autowired
    public ConnectionsController(@Qualifier("connectionsServiceImpl") ConnectionsService connectionsService) {
        this.connectionsService = connectionsService;
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ConnectionsDTO>> getConnections() {
        List<ConnectionsEntity> connectionsEntities = connectionsService.findAll();

        List<ConnectionsDTO> connectionsDTOS = getListOfConnectionDTO(connectionsEntities);

        return ResponseEntity.ok(connectionsDTOS);
    }

    @GetMapping("/server-name/{connectionID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getServerAddress(@PathVariable Long connectionID) {
        return null;
    }

    @GetMapping("/port/{connectionID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Integer> getPort(@PathVariable Long connectionID) {
        return null;
    }

    @GetMapping("/username/{connectionID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getUserName(@PathVariable Long connectionID) {
        return null;
    }

    @PutMapping("/{connectionID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateConnection(@PathVariable Long connectionID) {
        return null;
    }

    @PostMapping("/testConnection")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> testConnection(@RequestBody ConnectionRequest connectionRequest)
    {
        LOGGER.info("Server: " + connectionRequest.getDbServer());
        LOGGER.info("Port: " + connectionRequest.getDbPort());
        LOGGER.info("DBUser: " + connectionRequest.getDbUser());
        LOGGER.info("DBPassword: " + connectionRequest.getDbPass());
        LOGGER.info("DBType: " + connectionRequest.getDbType());
        if (connectionsService.testConnection(connectionRequest)) {
            return ResponseEntity.ok("Database connection is successful.");
        } else {
            return ResponseEntity.status(500).body("Failed to connect to the database.");
        }
    }

    @PostMapping("/connect")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> connect(@RequestBody ConnectionRequest connectionRequest)
    {
        // Validate the Connection Request
        LOGGER.info("DatabaseName: " + connectionRequest.getDbName());
        validateConnectionRequest(connectionRequest);

        // Does the database name in the request already exist?
        LOGGER.info("Database: " + connectionRequest.getDbName() + " Exists: " + connectionsService.databaseNameExists(connectionRequest));
        if(connectionsService.databaseNameExists(connectionRequest))
        {
            // If the database already exists, connect to the database
            connectionsService.connectToDB(createConnectionEntity(connectionRequest));

            return ResponseEntity.ok("Successfully Connected to Database: " + connectionRequest.getDbName());
        }
        // Else if the database name doesn't exist, create a new database with that name
        else {
            // Create the Database
            connectionsService.createDatabase(createConnectionEntity(connectionRequest));

            // Connect to the database
            connectionsService.connectToDB(createConnectionEntity(connectionRequest));

            return ResponseEntity.ok("Successfully Created Database: " + connectionRequest.getDbName() + " on " + connectionRequest.getDbServer());
        }
    }

    @PostMapping("/database/validateName")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> checkDatabaseNameExists(@RequestBody @Valid ConnectionRequest connectionRequest)
    {
        if(connectionsService.databaseNameExists(connectionRequest))
        {
            return ResponseEntity.ok("Database: " + connectionRequest.getDbName() + " Exists");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Database: " + connectionRequest.getDbName() + " Not Found.");

    }

    private void validateConnectionRequest(ConnectionRequest connectionRequest)
    {
        Objects.requireNonNull(connectionRequest.getDbName(), "Database Name must not be null.");
        Objects.requireNonNull(connectionRequest.getDbUser(), "Database User must not be null.");
        Objects.requireNonNull(connectionRequest.getDbServer(), "Database Server must not be null.");
        Objects.requireNonNull(connectionRequest.getDbPass(), "Database Password must not be null.");
        Objects.requireNonNull(connectionRequest.getDbType(), "Database Type must not be null.");
    }

    private ConnectionsEntity createConnectionEntity(ConnectionRequest connectionsDTO)
    {
        ConnectionsEntity connectionsEntity = new ConnectionsEntity();
        connectionsEntity.setDbServer(connectionsDTO.getDbServer());
        connectionsEntity.setDbPort(connectionsDTO.getDbPort());
        connectionsEntity.setDbName(connectionsDTO.getDbName());
        connectionsEntity.setDbUser(connectionsDTO.getDbUser());
        connectionsEntity.setDbPass(connectionsDTO.getDbPass());
        connectionsEntity.setDbType(DBType.valueOf(connectionsDTO.getDbType()));
        return connectionsEntity;
    }
}
