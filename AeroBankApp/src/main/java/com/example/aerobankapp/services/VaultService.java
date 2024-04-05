package com.example.aerobankapp.services;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Map;

public interface VaultService
{
    SecretKey getSecretKeyById(String keyId);

    void storeSecret(String path, Object secret);

    void deleteSecret(String path);

    SecretKey rotateKey(String keyName);

    String generateEncryptionKey(String keyName);

    Map<String, Object> getSecrets(String path);

    List<String> listSecrets(String path);

    Map<String, Object> getDatabaseCredentials(String role);

    String decryptData(String keyName, String cipherText);

    void renewToken(String token);
    void revokeToken(String token);
}
