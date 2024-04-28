package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.AccountDetailsDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.services.AccountCodeService;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.AccountPropertiesService;
import com.example.aerobankapp.services.AccountServiceImpl;
import com.example.aerobankapp.workbench.*;
import com.example.aerobankapp.workbench.utilities.AccountCreationRequest;
import com.example.aerobankapp.workbench.utilities.AccountNameResponse;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.aerobankapp.controllers.utils.AccountControllerUtil.*;

@RestController
@RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {
    private final AccountServiceImpl accountDAO;
    private final AccountPropertiesService accountPropertiesService;
    private final AccountNotificationService accountNotificationService;
    private final AccountCodeService accountCodeService;
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    public AccountController(AccountServiceImpl accountDAO, AccountPropertiesService accountPropertiesService,
                             AccountNotificationService accountNotificationService,
                             AccountCodeService accountCodeService) {
        this.accountDAO = accountDAO;
        this.accountPropertiesService = accountPropertiesService;
        this.accountNotificationService = accountNotificationService;
        this.accountCodeService = accountCodeService;
    }

    @GetMapping("/data/{user}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getUserAccounts(@PathVariable String user) {
        List<AccountEntity> accountEntities = accountDAO.findByUserName(user);
        List<AccountPropertiesEntity> accountPropertiesEntities = accountPropertiesService.findByUserName(user);

        List<String> accountCodeShortSegments = accountDAO.getAccountCodeShortSegmentByUser(user);

        accountEntities.forEach(results -> System.out.println(results.getClass().getName()));
        List<AccountResponse> accountResponseList = getAccountResponseList(accountPropertiesEntities, accountCodeShortSegments, accountEntities, new BigDecimal("1200"), new BigDecimal("1150"));

        return ResponseEntity.ok(accountResponseList);
    }

    @GetMapping("/codes/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> fetchAccountCodesByAccountNumber(@PathVariable String accountNumber){
        List<String> accountCodes = accountDAO.getAccountNamesWithAcctSegmentByAccountNumber(accountNumber);
        List<AccountCodeResponse> accountCodeResponseList = getAccountCodesAsResponse(accountCodes);
        return ResponseEntity.ok(accountCodeResponseList);
    }

    @GetMapping("/codes/list/{accountNumber}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> fetchAccountCodeIDListByAccountNumber(@PathVariable String accountNumber){
        List<Integer> accountCodeIds = accountDAO.getListOfAccountCodeIDsByAcctNumber(accountNumber);
        List<AccountCodeIDResponse> accountCodeIDResponses = getAccountCodeIDResponseList(accountCodeIds);
        return ResponseEntity.ok(accountCodeIDResponses);
    }

    @GetMapping("/list/{userID}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> fetchAccountNamesByUserID(@PathVariable int userID){
        List<AccountEntity> accountEntities = accountDAO.getListOfAccountsByUserID(userID);

        List<String> accountNames = accountEntities.stream()
                .map(AccountEntity::getAccountName)
                .filter(Objects::nonNull)
                .toList();

        List<AccountNameResponse> accountNamesList = getAccountNamesResponseList(accountNames);
        return ResponseEntity.ok(accountNamesList);
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

    @GetMapping("/id/{accountName}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> fetchAccountIDFromAccountName(@PathVariable String accountName){
        int accountID = accountDAO.getAccountIDByAccountName(accountName);
        return ResponseEntity.ok(new AccountIDResponse(accountID));
    }

    @GetMapping("/{userID}/{accountCodeID}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> getAccountIDByUserIDAndAccountCode(@PathVariable int userID, @PathVariable Long accountCodeID)
    {
        if(accountCodeID < 1L || accountDAO.isInvalidUserID(userID))
        {
            return ResponseEntity.badRequest().body("Invalid User ID or accountCode");
        }
        int receivedAccountID = accountDAO.getAccountIDByAcctCodeAndUserID(userID, accountCodeID);
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

    @GetMapping("/notifications/{acctID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAccountNotifications(@PathVariable int acctID){

        List<AccountNotificationEntity> accountNotificationEntities = accountNotificationService.getUnreadAccountNotifications(acctID);
        List<AccountNotificationResponse> accountNotificationResponses = getAccountNotificationResponseList(accountNotificationEntities);
        return ResponseEntity.ok(accountNotificationResponses);
    }

    /**
     * This Method will be used for testing the account notifications on the front-end
     * @return
     */
    @PostMapping("/notifications/test")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> sendTestRequest(@RequestBody NotificationRequest notificationRequest){

        AccountEntity account = AccountEntity.builder()
                .acctID(notificationRequest.getAccountID())
                .build();

        AccountNotificationEntity accountNotificationEntity = new AccountNotificationEntity(account, notificationRequest.getTitle(), notificationRequest.getMessage(), notificationRequest.getPriority(), notificationRequest.isRead(), notificationRequest.isSevere(), notificationRequest.getAccountNotificationCategory());

        accountNotificationService.save(accountNotificationEntity);
        return ResponseEntity.ok("Account Notification Saved Successfully.");
    }

    @GetMapping("/notifications/unread/{acctID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchUnreadAccountNotifications(@PathVariable int acctID){
        return null;
    }

    @GetMapping("/notifications/read/{acctID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchReadAccountNotifications(@PathVariable int acctID){
        return null;
    }

    @GetMapping("/notifications/category")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchAccountNotificationsByCategory(@RequestParam AccountNotificationCategory category){
        return null;
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

    @DeleteMapping("/notifications/{acctID}/{notificationID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteNotification(@PathVariable int acctID, @PathVariable Long notificationID){
        accountNotificationService.deleteAccountNotification(acctID, notificationID);
        return ResponseEntity.ok("Deleted AccountNotification with acctID: " + acctID + " and accountNotificationID: " + notificationID);
    }

    @PutMapping("/notifications/update/{acctID}/{notificationID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateAccountNotificationAsRead(@PathVariable int acctID, @PathVariable Long notificationID){
        accountNotificationService.updateAccountNotificationAsRead(acctID, notificationID);
        return ResponseEntity.ok("Updated Account Notification with acctID: " + acctID + " and notificationID: " + notificationID + " as read");
    }


    private boolean isInvalidAccountCode(String accountCode)
    {
        return accountCode == null || accountCode.length() != 2;
    }
}
