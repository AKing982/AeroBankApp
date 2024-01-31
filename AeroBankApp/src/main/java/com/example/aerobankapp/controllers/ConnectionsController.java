package com.example.aerobankapp.controllers;


import com.example.aerobankapp.dto.ConnectionsDTO;
import com.example.aerobankapp.services.ConnectionsService;
import com.example.aerobankapp.services.ConnectionsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value="/api/connections")
@CrossOrigin(value="http://localhost:3000")
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @Autowired
    public ConnectionsController(ConnectionsService connectionsService) {
        this.connectionsService = connectionsService;
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ConnectionsDTO>> getConnections() {
        return null;
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
    public ResponseEntity<?> updateConnection(@PathVariable Long connectionID)
    {
        return null;
    }
}
