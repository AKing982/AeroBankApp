package com.example.aerobankapp.services;

public interface FinancialEncryptionService extends EncryptionService
{
    String encryptWithAE5(String plain);
}
