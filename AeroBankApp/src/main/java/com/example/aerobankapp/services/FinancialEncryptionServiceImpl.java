package com.example.aerobankapp.services;

import com.example.aerobankapp.exceptions.KeyGenerationException;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@Getter
@Setter
public class FinancialEncryptionServiceImpl implements FinancialEncryptionService
{
    private final VaultTemplate vaultTemplate;
    private Gson gson;

    @Autowired
    public FinancialEncryptionServiceImpl(VaultTemplate vaultTemplate){
        this.vaultTemplate = vaultTemplate;
    }

    public SecretKey generateKey(int bits) throws KeyGenerationException
    {
        try
        {
            byte[] key = new byte[bits / 8];
            SecureRandom.getInstanceStrong().nextBytes(key);
            return new SecretKeySpec(key, "AES");

        }catch(NoSuchAlgorithmException ex)
        {
            throw new KeyGenerationException("Failed to generate AES Key.");
        }
    }

    @Override
    public String decrypt(String encryptedText, SecretKey key) throws Exception {
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
    public String rotateKey() {
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

    public SecretKey getCurrentKey(){
        return null;
    }

    public int getKeyRotationPeriod(){
        return 0;
    }

    public boolean isCurrentKey(final SecretKey key){
        return false;
    }
}
