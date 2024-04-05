package com.example.aerobankapp.services;

import com.example.aerobankapp.exceptions.InvalidStringException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.vault.authentication.AppRoleAuthentication;
import org.springframework.vault.authentication.AppRoleAuthenticationOptions;
import org.springframework.vault.client.VaultClients;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class VaultServiceImpl implements VaultService{

    private final String vaultUri = "http://localhost:8200/";
    private String roleId;
    private String secretId;
    private final VaultTemplate vaultTemplate;
    @Autowired
    public VaultServiceImpl(VaultTemplate vaultTemplate)
    {
        this.vaultTemplate = vaultTemplate;
    }

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

    @Override
    public SecretKey getSecretKeyById(String keyId) {
        if(!keyId.isEmpty()){
            final String path = "secret/data/" + keyId;
            VaultResponseSupport<SecretKey> response = vaultTemplate.read(path, SecretKey.class);
            if(response != null){
                return response.getData();
            }
        }
        throw new InvalidStringException("Caught invalid keyId");
    }

    @Override
    public void storeSecret(String path, Object secret) {
        if(path.isEmpty()){
            throw new InvalidStringException("Invalid Path.");
        }
        VaultKeyValueOperations keyValueOps = vaultTemplate.opsForKeyValue("secret", VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);
        keyValueOps.put(path, secret);
    }

    @Override
    public void deleteSecret(String path) {
        if(path.isEmpty()){
            throw new InvalidStringException("Invalid Path.");
        }
        VaultKeyValueOperations keyValueOps = vaultTemplate.opsForKeyValue("secret", VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);
        keyValueOps.delete(path);
    }

    @Override
    public SecretKey rotateKey(String keyName) {
        String rotateKeyPath = "transit/keys/" + keyName + "/rotate";
        vaultTemplate.write(rotateKeyPath, null);
        return null;
    }

    @Override
    public String generateEncryptionKey(String keyName) {
        VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();
        return transitOperations.encrypt(keyName, "plainText");
    }

    @Override
    public Map<String, Object> getSecrets(String path) {
        return null;
    }

    @Override
    public List<String> listSecrets(String path) {
        if(path.isEmpty()){
            throw new InvalidStringException("Invalid path found.");
        }
        return vaultTemplate.list("secret/metadata/" + path);
    }

    @Override
    public Map<String, Object> getDatabaseCredentials(String role) {
        return null;
    }

    @Override
    public String decryptData(String keyName, String cipherText) {
        if(keyName.isEmpty()){
            throw new InvalidStringException("Invalid KeyName string caught.");
        }
        VaultTransitOperations transitOperations = vaultTemplate.opsForTransit();
        return transitOperations.decrypt(keyName, cipherText);
    }

    @Override
    public void renewToken(final String token) {
       int increment = 3600; // Renewal increment

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Vault-Token", token);
        HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(Collections.singletonMap("increment", increment), headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8200/v1/auth/token/renew-self",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
        }catch(RestClientException ex){
            throw new RuntimeException("Failed to renew token: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void revokeToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Vault-Token", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(
                    "http://localhost:8200/v1/auth/token/revoke-self",
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );

        }catch(RestClientException ex){
            throw new RuntimeException("Failed to Revoke Token: " + ex.getMessage(), ex);
        }
    }
}
