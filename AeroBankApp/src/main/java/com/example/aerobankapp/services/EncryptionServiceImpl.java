package com.example.aerobankapp.services;

import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class EncryptionServiceImpl implements EncryptionService
{

    @Override
    public String encryptData(String plainText) {
        return null;
    }

    @Override
    public String decryptData(String cipher) {
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