package com.example.aerobankapp.controllers;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.services.AccountDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController
{
    private final AccountDAOImpl accountDAO;

    @Autowired
    public AccountController(AccountDAOImpl accountDAO)
    {
        this.accountDAO = accountDAO;
    }

    @GetMapping("/data/{user}")
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public ResponseEntity<?> getUserAccounts(@PathVariable String user)
    {
        List<AccountEntity> accountEntities = accountDAO.findByUserName(user);
        return ResponseEntity.ok(accountEntities);
    }
}
