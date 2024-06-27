package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.plaid.PlaidService;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.model.LinkTokenCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value="/api/plaid")
@CrossOrigin(value="http://localhost:3000")
public class PlaidController {

    private final PlaidService plaidService;

    @Autowired
    public PlaidController(PlaidService plaidService)
    {
        this.plaidService = plaidService;
    }

    @PostMapping("/create_link_token")
    public ResponseEntity<?> createLinkToken(@RequestBody Map<String, String> request)
    {
        String userID = request.get("userId");

        try
        {
            LinkTokenCreateResponse linkTokenCreateResponse = plaidService.createLinkToken(userID);
            if(linkTokenCreateResponse == null || linkTokenCreateResponse.getLinkToken() == null)
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Link token creation failed");
            }
            return ResponseEntity.ok().body(Map.of("link_token", linkTokenCreateResponse.getLinkToken()));

        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/exchange_public_token")
    public ResponseEntity<?> exchangePublicToken(@RequestBody Map<String, String> request)
    {
        String publicToken = request.get("public_token");

        try
        {
            ItemPublicTokenExchangeResponse exchangeResponse = plaidService.exchangePublicToken(publicToken);

            //TODO: Save the access token and itemID to the database, associated with the user

            String accessToken = exchangeResponse.getAccessToken();
            String itemId = exchangeResponse.getItemId();

            return ResponseEntity.ok().body(Map.of("message", "Access Token retrieved successfully"));
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
