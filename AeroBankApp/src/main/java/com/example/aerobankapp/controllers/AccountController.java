package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.AccountDetailsDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.services.AccountServiceImpl;
import com.example.aerobankapp.workbench.utilities.AccountCreationRequest;
import com.example.aerobankapp.workbench.utilities.response.AccountResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.example.aerobankapp.controllers.utils.AccountControllerUtil.getAccountCodesAsResponse;
import static com.example.aerobankapp.controllers.utils.AccountControllerUtil.getAccountResponseList;

@RestController
@RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {
    private final AccountServiceImpl accountDAO;

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    public AccountController(AccountServiceImpl accountDAO) {
        this.accountDAO = accountDAO;
    }

    @GetMapping("/data/{user}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getUserAccounts(@PathVariable String user) {
        List<AccountEntity> accountEntities = accountDAO.findByUserName(user);
        accountEntities.forEach(results -> System.out.println(results.getClass().getName()));
        List<AccountResponse> accountResponseList = getAccountResponseList(accountEntities, new BigDecimal("1200"), new BigDecimal("1150"));

        return ResponseEntity.ok(accountResponseList);
    }

    @GetMapping("/data/codes/{user}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getListOfExistingAccountCodes(@PathVariable String user) {
        List<String> accountCodes = accountDAO.getListOfAccountCodes(user);
        List<AccountResponse> accountResponseList = getAccountCodesAsResponse(accountCodes);

        return ResponseEntity.ok(accountResponseList);
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        return null;
    }

    @GetMapping("/{accountID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AccountDetailsDTO>> getAccountDetails(@PathVariable Long accountID)
    {
        return null;
    }

    @PostMapping("/{accountID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateAccount(@PathVariable Long accountID)
    {
        return null;
    }

    @DeleteMapping("/{accountID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> closeAccount(@PathVariable Long accountID)
    {
        return null;
    }

    @GetMapping("/account/{accountCode}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AccountDTO>> getAllAccountsByCode(@PathVariable String accountCode)
    {
        return null;
    }

    @GetMapping("/{accountID}/balance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BigDecimal> checkBalance(@PathVariable Long accountID)
    {
        return null;
    }

    @GetMapping("/{accountID}/pending")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BigDecimal> pendingBalance(@PathVariable Long accountID)
    {
        return null;
    }







}
