package com.example.aerobankapp.configuration;

import com.plaid.client.request.PlaidApi;
import com.plaid.client.ApiClient;
import okhttp3.OkHttpClient;
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
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            okhttp3.Request original = chain.request();
            okhttp3.Request request = original.newBuilder()
                    .header("PLAID-CLIENT-ID", plaidClientId)
                    .header("PLAID-SECRET", plaidSecret)
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });

        ApiClient apiClient = new ApiClient();
        apiClient.setPlaidAdapter("https://sandbox.plaid.com");
        apiClient.configureFromOkclient(httpClient.build());

        return apiClient.createService(PlaidApi.class);
    }
}
