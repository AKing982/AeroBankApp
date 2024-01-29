package com.example.aerobankapp.controllers;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.services.AccountServiceImpl;
import com.example.aerobankapp.workbench.utilities.response.AccountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController
{
    private final AccountServiceImpl accountDAO;

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    public AccountController(AccountServiceImpl accountDAO)
    {
        this.accountDAO = accountDAO;
    }

    @GetMapping("/data/{user}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getUserAccounts(@PathVariable String user)
    {
        List<AccountEntity> accountEntities = accountDAO.findByUserName(user);
        accountEntities.forEach(results -> System.out.println(results.getClass().getName()));
        List<AccountResponse> accountResponseList = getAccountResponseList(accountEntities, new BigDecimal("1200"), new BigDecimal("1150"));

        return ResponseEntity.ok(accountResponseList);
    }

    @GetMapping("/data/codes/{user}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getListOfExistingAccountCodes(@PathVariable String user)
    {
        List<String> accountCodes = accountDAO.getListOfAccountCodes(user);
        List<AccountResponse> accountResponseList = getAccountCodesAsResponse(accountCodes);

        return ResponseEntity.ok(accountResponseList);
    }

    private List<AccountResponse> getAccountCodesAsResponse(List<String> accountCodes)
    {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(String code : accountCodes)
        {
            AccountResponse accountResponse = new AccountResponse(code);
            accountResponseList.add(accountResponse);
        }

        return accountResponseList;
    }

    private List<AccountResponse> getAccountResponseList(List<AccountEntity> entityList, BigDecimal pending, BigDecimal available)
    {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(AccountEntity entity : entityList)
        {
            logger.warn("Entity: " + entity.getClass().getName());
            BigDecimal balance = entity.getBalance();
            String acctCode = entity.getAccountCode();
            AccountResponse accountResponse = new AccountResponse(acctCode, balance, pending, available);
            accountResponseList.add(accountResponse);
        }

        return accountResponseList;
    }
}
