package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.PlaidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/plaid")
public class PlaidController
{
    private final PlaidService plaidService;

    @Autowired
    public PlaidController(PlaidService plaidService)
    {
        this.plaidService = plaidService;
    }

    @PostMapping("/exchange_token")
    public String exchangeToken(@RequestBody String publicToken)
    {
        return plaidService.exchangePublicToken(publicToken);
    }

}
