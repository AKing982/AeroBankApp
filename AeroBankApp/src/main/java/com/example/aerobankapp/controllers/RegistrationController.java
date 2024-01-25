package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.UserDAOImpl;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/registration", method= RequestMethod.POST)
@CrossOrigin(origins = "http://localhost:3000")
@Getter
public class RegistrationController
{

    private final UserDAOImpl userDAO;

    @Autowired
    public RegistrationController(UserDAOImpl userDAO)
    {
        this.userDAO = userDAO;
    }

    @PostMapping(value="/register")
    public ResponseEntity<?> sendRegistrationRequest(@Valid @RequestBody RegistrationDTO request)
    {
        UserEntity userEntity = getUserDAO().registerUser(request);
        return ResponseEntity.ok().body(userEntity);
    }

}
