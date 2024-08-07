package com.example.aerobankapp.services;

import com.example.aerobankapp.exceptions.KeyGenerationException;

import javax.crypto.SecretKey;
import java.security.Key;

public interface EncryptionService
{
    String decrypt(String cipher, SecretKey key) throws Exception;

    SecretKey rotateKey() throws KeyGenerationException;

    Key getEncryptionKey(String keyId);

    boolean validateEncryptionSetup();
}
