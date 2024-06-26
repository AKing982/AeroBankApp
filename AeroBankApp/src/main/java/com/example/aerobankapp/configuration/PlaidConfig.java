package com.example.aerobankapp.configuration;

import com.plaid.client.request.PlaidApi;
import com.plaid.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.plaid.client.ServerConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaidConfig {

    @Value("${plaid.client-id}")
    private String plaidClientId;

    @Value("${plaid.secret}")
    private String plaidSecret;

    @Bean
    public PlaidApi plaidApi()
    {
        ApiClient apiClient = ApiClient.newBuilder()
    }
}
