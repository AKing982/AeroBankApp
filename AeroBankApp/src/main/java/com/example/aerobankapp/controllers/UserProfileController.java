package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.response.UserProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

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
        LOGGER.info("User Profile for: {}", username);
        BigDecimal totalBalances = accountDAO.getTotalAccountBalances(username);
        String accountNumber = userDAO.getAccountNumberByUserName(username);
        Long numberOfAccounts = accountDAO.getNumberOfAccounts(username);
        int userID = userDAO.getUserIDByUserName(username);
        Role role = userDAO.getUserRole(username);
        System.out.println("UserID: " + userID);

        return ResponseEntity.ok(new UserProfileResponse(userID, username, accountNumber, totalBalances, numberOfAccounts, role));
    }

}
