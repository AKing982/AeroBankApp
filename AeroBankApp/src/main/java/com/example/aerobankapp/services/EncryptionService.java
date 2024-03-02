package com.example.aerobankapp.services;

import java.security.Key;

public interface EncryptionService
{
    String encryptData(String plainText);

    String decryptData(String cipher);

    String rotateEncryption();

    Key getEncryptionKey(String keyId);

    boolean validateEncryptionSetup();
}
