package com.example.aerobankapp.services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class FinancialEncryptionServiceImpl implements FinancialEncryptionService
{
    public static SecretKey generateKey(int n) throws Exception
    {
        byte[] key = new byte[n / 8];
        SecureRandom.getInstanceStrong().nextBytes(key);
        return new SecretKeySpec(key, "AES");
    }

    @Override
    public String decryptData(String encryptedText, SecretKey key) throws Exception {
        byte[] encryptedIvTextBytes = Base64.getDecoder().decode(encryptedText);

        byte[] iv = new byte[16];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        int encryptedSize = encryptedIvTextBytes.length - iv.length;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, iv.length, encryptedBytes, 0, encryptedSize);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(encryptedBytes);

        return new String(decrypted, "UTF-8");
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

    @Override
    public String encryptWithAE5(String plain, SecretKey key) throws Exception {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(plain.getBytes("UTF-8"));

        byte[] encryptedIVAndText = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, iv.length);
        System.arraycopy(encrypted, 0, encryptedIVAndText, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedIVAndText);
    }
}
