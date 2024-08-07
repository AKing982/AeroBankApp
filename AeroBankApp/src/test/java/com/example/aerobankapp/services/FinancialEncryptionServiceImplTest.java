package com.example.aerobankapp.services;

import com.example.aerobankapp.exceptions.InvalidUserStringException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.vault.core.VaultTemplate;

import javax.crypto.SecretKey;

import java.math.BigDecimal;
import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class FinancialEncryptionServiceImplTest {

    @InjectMocks
    private FinancialEncryptionServiceImpl financialEncryptionService;

    @Autowired
    private VaultService vaultService;

    private SecretKey secretKey;

    @BeforeEach
    void setUp() throws Exception{
        financialEncryptionService = new FinancialEncryptionServiceImpl(vaultService);
        secretKey = financialEncryptionService.generateKey(128);
    }


    @Test
    public void testEncryptDecryptBigDecimal() throws Exception{
        BigDecimal balance = new BigDecimal("1215.000");
        String encryptedValue = financialEncryptionService.encryptWithAE5(balance.toString(), secretKey);
        String decryptedValue = financialEncryptionService.decrypt(encryptedValue, secretKey);
        BigDecimal decryptedDecimal = new BigDecimal(decryptedValue);

        assertEquals(balance, decryptedDecimal);
    }

    @Test
    public void testEncryptDecryptBigDecimal_InvalidKey() throws Exception{

    }

    @Test
    public void testEncryptWithEmptyString(){
        assertThrows(InvalidUserStringException.class, () -> {
            financialEncryptionService.encryptWithAE5("", secretKey);
        });
    }

    @Test
    public void testGenerateKey_invalidBits_throwsException(){
        final int bits = -1;
        assertThrows(IllegalArgumentException.class, () -> {
            financialEncryptionService.generateKey(bits);
        });
    }

    @Test
    public void testEncryptWithNullKey_throwsException(){
        assertThrows(InvalidKeyException.class, () -> {
            financialEncryptionService.encryptWithAE5("Some plainText", null);
        });
    }

    @Test
    public void testDecryptWithNullKey_throwsException(){
        assertThrows(InvalidKeyException.class, () -> {
            financialEncryptionService.decrypt("sjklsjls", null);
        });
    }

    @Test
    public void testRotateKey() throws Exception{
        SecretKey originalKey = financialEncryptionService.generateKey(256);

        SecretKey rotatedKey = financialEncryptionService.rotateKey();

        SecretKey newKey = financialEncryptionService.getCurrentKey();

        assertNotEquals(originalKey, rotatedKey);
    }

    @Test
    public void testGetEncryptionKey_EmptyId(){

    }

    @AfterEach
    void tearDown() {
    }
}