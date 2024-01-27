package com.example.aerobankapp.controllers;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.services.AccountDAOImpl;
import com.example.aerobankapp.workbench.utilities.response.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
       // List<AccountResponse> accountResponseList = getAccountResponseList(accountEntities);

        return ResponseEntity.ok(accountEntities);
    }

    private List<AccountResponse> getAccountResponseList(List<AccountEntity> entityList, BigDecimal pending, BigDecimal available)
    {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(AccountEntity entity : entityList)
        {
            BigDecimal balance = entity.getBalance();
            String acctCode = entity.getAccountCode();
            AccountResponse accountResponse = new AccountResponse(acctCode, balance, pending, available);
            accountResponseList.add(accountResponse);
        }

        return accountResponseList;
    }
}
