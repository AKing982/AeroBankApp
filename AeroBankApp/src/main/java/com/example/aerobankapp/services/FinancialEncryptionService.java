package com.example.aerobankapp.services;

import javax.crypto.SecretKey;

public interface FinancialEncryptionService extends EncryptionService
{
    String encryptWithAE5(String plain, SecretKey key) throws Exception;
}
