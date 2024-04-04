package com.example.aerobankapp.services;

import javax.crypto.SecretKey;
import java.security.Key;

public interface EncryptionService
{
    String decryptData(String cipher, SecretKey key) throws Exception;

    String rotateEncryption();

    Key getEncryptionKey(String keyId);

    boolean validateEncryptionSetup();
}
