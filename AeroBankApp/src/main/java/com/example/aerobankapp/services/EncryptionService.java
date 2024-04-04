package com.example.aerobankapp.services;

import javax.crypto.SecretKey;
import java.security.Key;

public interface EncryptionService
{
    String decrypt(String cipher, SecretKey key) throws Exception;

    String rotateKey();

    Key getEncryptionKey(String keyId);

    boolean validateEncryptionSetup();
}
