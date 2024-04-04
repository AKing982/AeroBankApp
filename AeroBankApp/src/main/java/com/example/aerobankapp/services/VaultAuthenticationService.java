package com.example.aerobankapp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.vault.authentication.AppRoleAuthentication;
import org.springframework.vault.authentication.AppRoleAuthenticationOptions;
import org.springframework.vault.client.VaultClients;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.ClientOptions;
import org.springframework.vault.support.SslConfiguration;
import org.springframework.vault.support.VaultToken;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class VaultAuthenticationService {

    @Value("${vault.uri}")
    private String vaultUri;

    @Value("${vault.roleId}")
    private String roleId;

    @Value("${vault.secretId}")
    private String secretId;

    public VaultTemplate createVaultTemplate(){
        VaultEndpoint endpoint = VaultEndpoint.from(URI.create(vaultUri));

        AppRoleAuthenticationOptions options = AppRoleAuthenticationOptions.builder()
                .roleId(AppRoleAuthenticationOptions.RoleId.provided(roleId))
                .secretId(AppRoleAuthenticationOptions.SecretId.provided(secretId))
                .build();

        RestTemplate restTemplate = VaultClients.createRestTemplate();
        AppRoleAuthentication authentication = new AppRoleAuthentication(options, restTemplate);

        return new VaultTemplate(endpoint, authentication);
    }

}
