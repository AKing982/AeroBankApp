package com.example.aerobankapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

import java.net.URI;

@Configuration
public class VaultConfiguration {

    @Bean
    public VaultTemplate vaultTemplate(){
        return new VaultTemplate(VaultEndpoint.from(URI.create("http://localhost:8200")),
                new TokenAuthentication("MY_VAULT_TOKEN"));
    }
}
