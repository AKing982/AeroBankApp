package com.example.aerobankapp.controllers;

import com.example.aerobankapp.converter.AccountBaseToPlaidAccountConverter;
import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.exceptions.AccountNotFoundException;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.services.plaid.PlaidService;
import com.plaid.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/api/plaid")
@CrossOrigin(value="http://localhost:3000")
public class PlaidController {

    private final PlaidService plaidService;
    private final AccountBaseToPlaidAccountConverter accountBaseToPlaidAccountConverter;
    private Logger LOGGER = LoggerFactory.getLogger(PlaidController.class);

    @Autowired
    public PlaidController(PlaidService plaidService,
                           AccountBaseToPlaidAccountConverter accountBaseToPlaidAccountConverter)
    {
        this.plaidService = plaidService;
        this.accountBaseToPlaidAccountConverter = accountBaseToPlaidAccountConverter;
    }

    private boolean hasInvalidValue(Map<String, Integer> mapRequest)
    {
        return mapRequest.values()
                .stream()
                .anyMatch(value -> value == null || value < 1);
    }

    private boolean hasInvalidKey(Map<String, Integer> mapRequest)
    {
        return mapRequest.keySet()
                .stream()
                .anyMatch(key -> key == null || key.isEmpty());
    }

    @PostMapping("/create_link_token")
    public ResponseEntity<?> createLinkToken(@RequestBody Map<String, Integer> request)
    {
        if (request == null || request.isEmpty())
        {
            return ResponseEntity.badRequest().body("Invalid request");
        }

        int userID = request.get("userId");
        String userIDAsString = String.valueOf(userID);
        try
        {
            LinkTokenCreateResponse linkTokenCreateResponse = plaidService.createLinkToken(userIDAsString);
            if(linkTokenCreateResponse == null || linkTokenCreateResponse.getLinkToken() == null)
            {
                return getInternalServerErrorResponse("Link token creation failed");
            }
            return getStatusOkResponse(Map.of("link_token", linkTokenCreateResponse.getLinkToken()));

        }catch(Exception e)
        {
            return getInternalServerErrorResponse(e.getMessage());
        }
    }

    private Set<PlaidAccount> getPlaidAccountsSetFromResponse(final List<AccountBase> accounts)
    {
        Set<PlaidAccount> plaidAccounts = new HashSet<>();
        for(AccountBase account : accounts)
        {
            if(account != null)
            {
                PlaidAccount plaidAccount = accountBaseToPlaidAccountConverter.convert(account);
                if(plaidAccount != null)
                {
                    plaidAccounts.add(plaidAccount);
                }
            }
        }
        return plaidAccounts;
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> getAccounts(@RequestParam int userId)
    {
        if(userId < 1)
        {
            return ResponseEntity.badRequest().body("Invalid request");
        }

        try
        {
            Optional<PlaidAccountsEntity> plaidAccountsEntityOptional = plaidService.getPlaidAccountEntityByUserId(userId);
            if(plaidAccountsEntityOptional.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No Plaid account found for this user");
            }
            else
            {
                String accessToken = getAccessTokenFromEntity(plaidAccountsEntityOptional);
                if(accessToken == null || accessToken.isEmpty())
                {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No access token found for this user");
                }
                AccountsGetResponse accountsGetResponse = plaidService.getAccounts(accessToken);
                if(accountsGetResponse == null)
                {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No account found for this user");
                }
                Set<PlaidAccount> plaidAccounts = getPlaidAccountsSetFromResponse(accountsGetResponse.getAccounts());
                return getStatusOkResponse(plaidAccounts);
            }

        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFoundException(AccountNotFoundException e)
    {
        return e.getMessage();
    }

    @GetMapping("/balances")
    public ResponseEntity<?> getBalances(@RequestParam int userId)
    {
        if (userId < 1)
        {
            return ResponseEntity.badRequest().body("Invalid request");
        }

        try
        {
            Optional<PlaidAccountsEntity> mockOptional = plaidService.getPlaidAccountEntityByUserId(userId);
            if (mockOptional.isEmpty())
            {
                return getInternalServerErrorResponse("No Plaid account found for this user");
            }
            else
            {
                String accessToken = getAccessTokenFromEntity(mockOptional);
                if(accessToken == null || accessToken.isEmpty())
                {
                    return getInternalServerErrorResponse("No access token found for this user");
                }
                AccountsGetResponse accountsGetResponse = plaidService.getAccounts(accessToken);
                if(accountsGetResponse == null)
                {
                    return getInternalServerErrorResponse("No account found for this user");
                }
                else
                {
                    List<AccountBase> accounts = accountsGetResponse.getAccounts();
                    if(accounts.isEmpty())
                    {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }
                    return getStatusOkResponse(accounts);
                }

            }
        }catch(Exception e)
        {
            return getInternalServerErrorResponse("Failed to get balances");
        }
    }

    private ResponseEntity<?> getStatusOkResponse(Collection<?> objects)
    {
        return ResponseEntity.ok().body(objects);
    }

    private ResponseEntity<?> getStatusOkResponse(Map<Object, Object> objectObjectMap)
    {
        return ResponseEntity.ok().body(objectObjectMap);
    }

    private ResponseEntity<?> getInternalServerErrorResponse(String message)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    private String getAccessTokenFromEntity(Optional<PlaidAccountsEntity> plaidAccountsEntityOptional)
    {
        return plaidAccountsEntityOptional.get().getAccessToken();
    }

    /**
     * Retrieves the transactions for a given user within a specified date range.
     *
     * @param userId The ID of the user.
     * @param startDate The start date of the transaction range.
     * @param endDate The end date of the transaction range.
     * @return A ResponseEntity object representing the result of the request.
     * @throws Exception if an error occurs while retrieving the transactions.
     */
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@RequestParam int userId,
                                             @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
                                             @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) throws Exception {
        if(userId < 1 || startDate == null || endDate == null)
        {
            return ResponseEntity.badRequest().body("Invalid request");
        }

        try
        {
            Optional<PlaidAccountsEntity> plaidAccountsEntityOptional = plaidService.getPlaidAccountEntityByUserId(userId);
            if(plaidAccountsEntityOptional.isEmpty())
            {
                return getInternalServerErrorResponse("No account found for this user");
            }
            else
            {
                String accessToken = getAccessTokenFromEntity(plaidAccountsEntityOptional);
                if(accessToken == null || accessToken.isEmpty())
                {
                    return getInternalServerErrorResponse("No access token found for this user");
                }
                TransactionsGetResponse transactionsGetResponse = plaidService.getTransactions(accessToken, startDate, endDate);
                return createTransactionResponse(transactionsGetResponse);
            }
        }catch(Exception e)
        {
            return getInternalServerErrorResponse("Internal Server Error");
        }
    }


    private ResponseEntity<?> createTransactionResponse(final TransactionsGetResponse transactionsGetResponse)
    {
        if(transactionsGetResponse == null)
        {
            return getInternalServerErrorResponse("No transactions found for this user");
        }
        else
        {
            //TODO: Convert the Transactions from getTransactions to PlaidTransactions and persist to database
            return ResponseEntity.ok().body(transactionsGetResponse.getTransactions());
        }
    }


    @PostMapping("/exchange_public_token")
    public ResponseEntity<?> exchangePublicToken(@RequestBody Map<String, Object> request)
    {
        String publicToken = (String) request.get("public_token");
        int userId = Integer.parseInt((String) request.get("userId"));
        LOGGER.info("User ID: {}", userId);
        Map<String, Object> metadata = (Map<String, Object>) request.get("metadata");

        try
        {
            ItemPublicTokenExchangeResponse exchangeResponse = plaidService.exchangePublicToken(publicToken);

            //TODO: Save the access token and itemID to the database, associated with the user

            String accessToken = exchangeResponse.getAccessToken();
            String itemId = exchangeResponse.getItemId();
            plaidService.createAndSavePlaidAccountEntity(itemId, userId, accessToken);

            return ResponseEntity.ok().body(Map.of("message", "Access Token retrieved successfully"));
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
