package com.example.aerobankapp.services;

import com.plaid.client.request.PlaidApi;
import com.plaid.client.ApiClient;
import org.springframework.stereotype.Service;

@Service
public class PlaidService
{
    private PlaidApi plaidApi;

    public PlaidService()
    {

    }
}
