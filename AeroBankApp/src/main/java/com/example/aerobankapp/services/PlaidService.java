package com.example.aerobankapp.services;

import com.plaid.client.model.AccountsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Service
public class PlaidService
{
    private final RestTemplate restTemplate;
    private final String plaidUrl = "https://sandbox.plaid.com";

    @Autowired
    public PlaidService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public String exchangePublicToken(String publicToken)
    {
        String url = plaidUrl + "/item/public_token/exchange";
        return "";
    }

    @GetMapping("/api/transactions")
    public String getTransactions(@RequestBody String token)
    {
        return "";
    }

    @GetMapping("/api/accounts")
    public AccountsGetResponse getAccounts(String accessToken)
    {
        AccountsGetResponse accountsGetResponse = null;
        return accountsGetResponse;
    }

    @GetMapping("/api/identity")
    public String getIdentity(@RequestBody String param)
    {
       return "";
    }

    @GetMapping("/api/balances")
    public String getBalances(@RequestBody String user, @RequestBody String acctID)
    {
        return "";
    }

    @GetMapping("/api/assetReport")
    public String getAssetReport(@RequestBody String report)
    {
        return "";
    }

    @PostMapping("/")

}
