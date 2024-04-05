package com.example.aerobankapp.services;

import com.example.aerobankapp.exceptions.InvalidStringException;
import com.example.aerobankapp.workbench.utilities.UserLogResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.support.VaultResponseSupport;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class VaultServiceImplTest {

    @InjectMocks
    private VaultServiceImpl vaultService;

    @Mock
    private VaultTemplate vaultTemplate;

    @Mock
    private VaultKeyValueOperations keyValueOps;

    @Mock
    private VaultTransitOperations transitOperations;

    @BeforeEach
    void setUp() {
        vaultService = new VaultServiceImpl(vaultTemplate);

        // Mock the opsForKeyValue to return the mocked VaultKeyValueOperations
        when(vaultTemplate.opsForKeyValue("secret", VaultKeyValueOperationsSupport.KeyValueBackend.KV_2))
                .thenReturn(keyValueOps);
    }

    @Test
    public void testGetSecretKeyById_EmptyKeyId(){
        assertThrows(InvalidStringException.class, () -> {
            vaultService.getSecretKeyById("");
        });
    }

    @Test
    public void testGetSecretKeyById_ValidKeyId(){
        String keyId = "123e4567-e89b-12d3-a456-426614174000";

        // Mocking the VaultResponseSupport
        VaultResponseSupport<SecretKey> mockResponse = new VaultResponseSupport<>();
        SecretKey expectedKey = mock(SecretKey.class); // Mocked SecretKey
        mockResponse.setData(expectedKey);

        // Mocking the read method of vaultTemplate
        when(vaultTemplate.read("secret/data/" + keyId, SecretKey.class)).thenReturn(mockResponse);

        // Calling the method under test
        SecretKey resultKey = vaultService.getSecretKeyById(keyId);

        // Assertions
        assertNotNull(resultKey, "The returned SecretKey should not be null.");
        assertEquals(expectedKey, resultKey, "The returned SecretKey should match the expected key.");
    }

    @Test
    public void testStoreSecret_InvalidPath_throwsException(){
        final String path = "";
        final Object secret = "MySecret";

        assertThrows(InvalidStringException.class, () -> {
            vaultService.storeSecret(path, secret);
        });
    }

    @Test
    public void testStoreSecret_ValidInput_ShouldInokePut(){
        String path = "mysecret";
        Object secret = new Object();

        vaultService.storeSecret(path, secret);

        verify(keyValueOps, times(1)).put(path, secret);
    }

    @Test
    public void testDeleteSecret_InvalidPath(){
        final String path = "";
        assertThrows(InvalidStringException.class, () -> {
            vaultService.deleteSecret(path);
        });
    }

    @Test
    public void testDeleteSecret_ValidPath(){
        String path = "mysecret";

        vaultService.deleteSecret(path);

        verify(keyValueOps, times(1)).delete(path);
    }

    @Test
    public void testRotateKey(){

    }

    @Test
    void testGenerateEncryptionKey_WithValidKeyName() {
        String keyName = "encryptionKey";
        String expectedCiphertext = "vault:v1:wrappedKeyCiphertext";

        when(vaultTemplate.opsForTransit()).thenReturn(transitOperations);
        when(transitOperations.encrypt(eq(keyName), anyString())).thenReturn(expectedCiphertext);

        String actualCiphertext = vaultService.generateEncryptionKey(keyName);

        assertEquals(expectedCiphertext, actualCiphertext);
        verify(transitOperations).encrypt(eq(keyName), anyString());
    }


    @AfterEach
    void tearDown() {
    }
}