package com.example.aerobankapp.services;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

@Service
public class CredentialEncryptionServiceImpl implements CredentialEncryptionService
{
    @Override
    public String encryptWithBCrypt(String plain) {
        return null;
    }


    @Override
    public String decryptData(String cipher, SecretKey key) {
        return null;
    }

    @Override
    public String rotateEncryption() {
        return null;
    }

    @Override
    public Key getEncryptionKey(String keyId) {
        return null;
    }

    @Override
    public boolean validateEncryptionSetup() {
        return false;
    }
}
