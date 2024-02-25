package com.example.aerobankapp.controllers;


import com.example.aerobankapp.dto.ConnectionsDTO;
import com.example.aerobankapp.entity.ConnectionsEntity;
import com.example.aerobankapp.services.ConnectionsService;
import com.example.aerobankapp.services.ConnectionsServiceImpl;
import com.example.aerobankapp.workbench.utilities.ConnectionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.aerobankapp.controllers.utils.ConnectionsControllerUtil.getListOfConnectionDTO;

@Controller
@RequestMapping(value="/api/connections", method = RequestMethod.GET)
@CrossOrigin(value="http://localhost:3000")
public class ConnectionsController {

    private final ConnectionsService connectionsService;

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
        return null;
    }
}
