package com.example.aerobankapp.services;

public interface CredentialEncryptionService extends EncryptionService
{
    String encryptWithBCrypt(String plain);
}
