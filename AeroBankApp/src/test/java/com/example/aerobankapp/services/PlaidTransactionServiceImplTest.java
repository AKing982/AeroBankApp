package com.example.aerobankapp.services;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.PlaidTransactionEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.repositories.PlaidTransactionRepository;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import({JpaConfig.class, AppConfig.class})
class PlaidTransactionServiceImplTest {

    @InjectMocks
    private PlaidTransactionServiceImpl plaidTransactionService;

    @Mock
    private PlaidTransactionRepository plaidTransactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        plaidTransactionService = new PlaidTransactionServiceImpl(plaidTransactionRepository);
    }

    @Test
    @DisplayName("Test save method when plaid transaction entity is null, then throw exception")
    public void testSave_whenPlaidTransactionisNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            plaidTransactionService.save(null);
        });
    }

    @Test
    @DisplayName("Test save method when plaid transaction parameters are null, then throw exception")
    public void testSave_whenPlaidTransactionParametersAreNull_thenThrowException() {
        PlaidTransactionEntity entity = createPlaidTransactionEntityWithNullParams();

        assertThrows(IllegalArgumentException.class, () -> {
            plaidTransactionService.save(entity);
        });
    }

    @Test
    @DisplayName("Test save method when plaid transactions parameters are valid, then save")
    public void testSave_whenPlaidTransactionParametersAreValid_thenSave() {
        PlaidTransactionEntity entity = createPlaidTransactionEntity();

        plaidTransactionService.save(entity);

        verify(plaidTransactionRepository, times(1)).save(entity);
    }

    @Test
    @DisplayName("Test delete method when plaid transaction is null, then throw exception")
    public void testDelete_whenPlaidTransactionIsNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            plaidTransactionService.delete(null);
        });
    }

    @Test
    @DisplayName("Test delete method when plaid transaction is valid")
    public void testDelete_whenPlaidTransactionIsValid_thenDelete() {

        PlaidTransactionEntity entity = createPlaidTransactionEntity();

        plaidTransactionService.delete(entity);

        verify(plaidTransactionRepository, times(1)).delete(entity);
    }

    @Test
    @DisplayName("Test getTransactionsByUser when user entity is null, then throw exception")
    public void testGetTransactionsByUser_whenUserEntityIsNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            plaidTransactionService.getTransactionsByUser(null);
        });
    }

    @Test
    @DisplayName("Test getTransactionsByUser when user entity valid, and userId is invalid, then throw exception for invalid userId")
    public void testGetTransactionsByUser_whenUserEntityIsValid_UserIdIsInvalid_thenThrowException() {
        assertThrows(InvalidUserIDException.class, () -> {
            plaidTransactionService.getTransactionsByUser(createUserEntityWithInvalidUserId());
        });
    }

    @Test
    @DisplayName("Test getTransactionsByUser when user entity is valid, and userId is valid, and transaction list is empty, then return Empty list")
    public void testGetTransactionsByUser_whenUserEntityIsValid_UserIdIsValid_AndTransactionListIsEmpty_thenReturnEmptyList() {
        List<PlaidTransactionEntity> plaidTransactionEntities = Collections.emptyList();
        when(plaidTransactionRepository.findByUserId(anyInt())).thenReturn(plaidTransactionEntities);

        List<PlaidTransactionEntity> actual = plaidTransactionService.getTransactionsByUser(createUserEntity());
        assertEquals(0, actual.size());
        assertNotNull(actual);
    }

    @Test
    @DisplayName("Test getTransactionsByUser when user entity is valid and userId is valid, and transaction list has invalid plaid transactions data, then throw exception")
    public void testGetTransactionsByUser

    private UserEntity createUserEntityWithInvalidUserId()
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserID(-1);
        return userEntity;
    }

    private UserEntity createUserEntity()
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserID(1);
        return userEntity;
    }

    private PlaidTransactionEntity createPlaidTransactionEntity() {


        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(new BigDecimal("100"));
        accountEntity.setAccountName("Test Checking");
        accountEntity.setUser(createUserEntity());
        accountEntity.setAcctID(1);

        PlaidTransactionEntity plaidTransactionEntity = new PlaidTransactionEntity();
        plaidTransactionEntity.setCreatedAt(LocalDateTime.now());
        plaidTransactionEntity.setId(1L);
        plaidTransactionEntity.setDate(LocalDate.of(2024, 6, 1));
        plaidTransactionEntity.setAmount(BigDecimal.valueOf(200));
        plaidTransactionEntity.setAccount(accountEntity);
        plaidTransactionEntity.setName("Test Transaction #33333");
        plaidTransactionEntity.setPending(false);
        plaidTransactionEntity.setUser(createUserEntity());
        plaidTransactionEntity.setExternalId("z7Qody755jfzBvXJxM9MCaRvWXDPBNilND1Wd");
        plaidTransactionEntity.setExternalAcctID("v4PezG4RRaIAGn7JPExEinZD1jrE3GtqeyEvZ");
        plaidTransactionEntity.setMerchantName("Plaid Transaction Merchant Name");
        plaidTransactionEntity.setAuthorizedDate(LocalDate.of(2024, 6, 4));
        return plaidTransactionEntity;
    }

    private PlaidTransactionEntity createPlaidTransactionEntityWithNullParams()
    {
        PlaidTransactionEntity plaidTransactionEntity = new PlaidTransactionEntity();
        plaidTransactionEntity.setCreatedAt(LocalDateTime.now());
        plaidTransactionEntity.setId(1L);
        plaidTransactionEntity.setDate(null);
        plaidTransactionEntity.setAmount(null);
        plaidTransactionEntity.setAccount(null);
        plaidTransactionEntity.setName(null);
        plaidTransactionEntity.setPending(false);
        plaidTransactionEntity.setUser(null);
        plaidTransactionEntity.setExternalId(null);
        plaidTransactionEntity.setExternalAcctID(null);
        plaidTransactionEntity.setMerchantName(null);
        plaidTransactionEntity.setAuthorizedDate(null);
        return plaidTransactionEntity;
    }

    @AfterEach
    void tearDown() {
    }
}