package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.*;
import jakarta.validation.Valid;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/registration", method= RequestMethod.POST)
@CrossOrigin(origins = "http://localhost:3000")
@Getter
public class RegistrationController
{

    private final RegistrationSubmitter registrationSubmitter;
    private final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    public RegistrationController(RegistrationSubmitter registrationSubmitter)
    {
        this.registrationSubmitter = registrationSubmitter;
    }

    @PostMapping(value="/register")
    public ResponseEntity<?> sendRegistrationRequest(@Valid @RequestBody RegistrationDTO request)
    {
        LOGGER.info("Registration Request Info: " + request.toString());
        registrationSubmitter.register(request);
        return ResponseEntity.ok("Registration Saved successfully.");
    }

}
