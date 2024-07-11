package com.example.aerobankapp.controllers;

import com.example.aerobankapp.converter.AccountBaseToPlaidAccountConverter;
import com.example.aerobankapp.entity.PlaidAccountsEntity;
import com.example.aerobankapp.exceptions.AccountNotFoundException;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.PlaidAccountBalances;
import com.example.aerobankapp.services.plaid.PlaidService;
import com.plaid.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/api/plaid")
@CrossOrigin(value="http://localhost:3000")
public class PlaidController {

    private final PlaidService plaidService;
    private Logger LOGGER = LoggerFactory.getLogger(PlaidController.class);

    @Autowired
    public PlaidController(PlaidService plaidService)
    {
        this.plaidService = plaidService;
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
            return getInternalServerErrorResponse(e.toString());
        }
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
            AccountsGetResponse accountsGetResponse = plaidService.getAccounts(userId);
            if(accountsGetResponse == null || accountsGetResponse.getAccounts() == null)
            {
                return ResponseEntity.noContent().build();
            }
            List<AccountBase> accountBaseList = accountsGetResponse.getAccounts();
            return ResponseEntity.ok().body(accountBaseList);

        }catch(Exception ex)
        {
            return getInternalServerErrorResponse(ex.toString());
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFoundException(AccountNotFoundException e)
    {
        return e.getMessage();
    }

    @GetMapping("/balances")
    public ResponseEntity<?> getBalances(@RequestParam int userId) throws Exception {
        if (userId < 1)
        {
            return ResponseEntity.badRequest().body("Invalid request");
        }
        List<PlaidAccountBalances> accountsGetResponse = plaidService.getAccountBalances(userId);
        if(accountsGetResponse.isEmpty())
        {
            return ResponseEntity.ok().body("No Balances found.");
        }
        return ResponseEntity.ok().body(accountsGetResponse);
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

       return ResponseEntity.ok().body(plaidService.getTransactions(userId, startDate, endDate));
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
