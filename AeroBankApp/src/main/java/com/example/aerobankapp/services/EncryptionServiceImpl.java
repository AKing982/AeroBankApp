package com.example.aerobankapp.services;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

public class EncryptionServiceImpl implements EncryptionService
{


    @Override
    public String decrypt(String cipher, SecretKey key) {
        return null;
    }

    @Override
    public SecretKey rotateKey() {
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
