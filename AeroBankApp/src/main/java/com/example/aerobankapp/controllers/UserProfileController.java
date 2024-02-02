package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.response.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value="/api/profile", method= RequestMethod.GET)
@CrossOrigin(value="http://localhost:3000")
public class UserProfileController {

    private UserServiceImpl userDAO;
    private AccountService accountDAO;

    @Autowired
    public UserProfileController(UserServiceImpl userDAO, AccountService accountDAO)
    {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
    }

    @GetMapping(value="/{username}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getUserProfileData(@PathVariable String username)
    {
        BigDecimal totalBalances = accountDAO.getTotalAccountBalances(username);
        String accountNumber = userDAO.getAccountNumberByUserName(username);
        Long numberOfAccounts = accountDAO.getNumberOfAccounts(username);
        Role role = userDAO.getUserRole(username);

        return ResponseEntity.ok(new UserProfileResponse(username, accountNumber, totalBalances, numberOfAccounts, role));
    }

}
