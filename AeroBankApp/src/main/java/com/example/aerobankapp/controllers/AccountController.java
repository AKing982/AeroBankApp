package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.AccountDetailsDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.services.AccountPropertiesService;
import com.example.aerobankapp.services.AccountServiceImpl;
import com.example.aerobankapp.workbench.AccountIDResponse;
import com.example.aerobankapp.workbench.utilities.AccountCreationRequest;
import com.example.aerobankapp.workbench.utilities.BalanceRequest;
import com.example.aerobankapp.workbench.utilities.response.AccountCodeResponse;
import com.example.aerobankapp.workbench.utilities.response.AccountResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.example.aerobankapp.controllers.utils.AccountControllerUtil.*;

@RestController
@RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {
    private final AccountServiceImpl accountDAO;
    private final AccountPropertiesService accountPropertiesService;
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    public AccountController(AccountServiceImpl accountDAO, AccountPropertiesService accountPropertiesService) {
        this.accountDAO = accountDAO;
        this.accountPropertiesService = accountPropertiesService;
    }

    @GetMapping("/data/{user}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getUserAccounts(@PathVariable String user) {
        List<AccountEntity> accountEntities = accountDAO.findByUserName(user);
        List<AccountPropertiesEntity> accountPropertiesEntities = accountPropertiesService.findByUserName(user);
        accountEntities.forEach(results -> System.out.println(results.getClass().getName()));
        List<AccountResponse> accountResponseList = getAccountResponseList(accountPropertiesEntities, accountEntities, new BigDecimal("1200"), new BigDecimal("1150"));

        return ResponseEntity.ok(accountResponseList);
    }

    @GetMapping("/data/codes/{user}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getListOfExistingAccountCodes(@PathVariable String user) {
        List<String> accountCodes = accountDAO.getListOfAccountCodes(user);
        List<AccountCodeResponse> accountCodeResponseList = getAccountCodesAsResponse(accountCodes);

        return ResponseEntity.ok(accountCodeResponseList);
    }

    @GetMapping("/{userName}/account-types")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<Map<Integer, String>> getAccountTypeMapByAccountID(@PathVariable String userName)
    {
        Map<Integer, String> accountTypeMap = accountDAO.getAccountTypeMapByAccountId(userName);

        return ResponseEntity.ok(accountTypeMap);
    }

    @GetMapping("/{userID}/{accountCode}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getAccountIDByUserIDAndAccountCode(@PathVariable int userID, @PathVariable String accountCode)
    {
        if(accountCode == null || accountDAO.isInvalidUserID(userID) || isInvalidAccountCode(accountCode))
        {
            return ResponseEntity.badRequest().body("Invalid User ID or accountCode");
        }
        int receivedAccountID = accountDAO.getAccountIDByAcctCodeAndUserID(userID, accountCode);
        if(receivedAccountID <= 0)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid AccountID found.");
        }
        logger.info("Received Account ID: " + receivedAccountID);
        return ResponseEntity.ok(new AccountIDResponse(receivedAccountID));
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

    @GetMapping("/rand/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRandomAccountIDByUserID(@PathVariable int userID){
        int acctID = accountDAO.getRandomAccountIDByUserID(userID);
        return ResponseEntity.ok(acctID);
    }

    @GetMapping("/maxTransactions/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAccountWithMostTransactionsByUserID(@PathVariable int userID){
        if(userID < 1){
            return ResponseEntity.badRequest().build();
        }
        Integer accountID = accountDAO.getAccountWithMostTransactionsByUserID(userID);
        return ResponseEntity.ok().body(accountID);
    }

    @PostMapping("/{accountID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateAccount(@PathVariable Long accountID)
    {
        return null;
    }

    @PutMapping("/{accountID}/balance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateAccountBalance(@PathVariable Long accountID, @RequestBody BalanceRequest request)
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


    private boolean isInvalidAccountCode(String accountCode)
    {
        return accountCode == null || accountCode.length() != 2;
    }


}
