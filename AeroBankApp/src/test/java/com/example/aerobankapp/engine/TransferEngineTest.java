package com.example.aerobankapp.engine;

import com.example.aerobankapp.converter.EntityToModelConverter;
import com.example.aerobankapp.converter.TransferConverter;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.NullTransferObjectException;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class TransferEngineTest {

    @InjectMocks
    private TransferEngine transferEngine;

    @MockBean
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountSecurityService accountSecurityService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CalculationEngine calculationEngine;

    @Autowired
    private BalanceHistoryService balanceHistoryService;

    @Autowired
    private EncryptionService encryptionService;

    private EntityToModelConverter<TransferEntity, Transfer> transferConverter = new TransferConverter();

    private TransferEntity transferEntity;
    private TransferEntity userTransfer;


    @BeforeEach
    void setUp() {
        transferEntity =  createMockTransfer(1L,
                false, new BigDecimal("45.00"),
                "Checking to Savings transfer",
                1, 1, 1, 2);

        userTransfer = createMockTransfer(2L, true, new BigDecimal("250.00"), "Transfer to BSmith",
                1, 2, 1, 4);


        transferEngine = new TransferEngine(transferService, accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
    }

    @Test
    public void testFindAllReturnsEmptyList(){
        when(transferService.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NonEmptyListRequiredException.class, () -> {
            transferEngine.fetchAll();
        });
    }

    @Test
    public void testFindAllReturnsNonEmptyList(){

        when(transferService.findAll()).thenReturn(List.of(transferEntity));

        List<Transfer> transfers = transferEngine.fetchAll();

        assertNotNull(transfers);
        assertFalse(transfers.isEmpty());
        assertEquals(1, transfers.size());
    }

    @Test
    public void testGetCalculatedAccountBalanceMap(){

    }


    @Test
    public void testRetrieveAccountIDsWithDistinctIDs(){
        List<TransferEntity> transferEntities = List.of(transferEntity, userTransfer);
        List<Transfer> transfers = transferEntities.stream().map(transferConverter::convert)
                .toList();

        Set<Integer> expectedFromAccountIDs = new HashSet<>(Arrays.asList(4, 2));
        Set<Integer> actualFromAccountIDs = transferEngine.retrieveTransferAccountIDSet(transfers, Transfer::getFromAccountID);
        assertEquals(expectedFromAccountIDs, actualFromAccountIDs);

        Set<Integer> expectedToAccountIDs = new HashSet<>(Arrays.asList(1));
        Set<Integer> actualToAccountIDs = transferEngine.retrieveTransferAccountIDSet(transfers, Transfer::getToAccountID);
        assertEquals(expectedToAccountIDs, actualToAccountIDs);
    }

    @Test
    public void testRetrieveAccountIDSetWithDuplicateIDs(){
        List<TransferEntity> transferEntities = List.of(transferEntity, userTransfer);
        List<Transfer> transfers = transferEntities.stream().map(transferConverter::convert)
                .toList();
    }

    @Test
    public void testRetrieveAccountIDsWithEmptyList(){
        List<Transfer> transfers = new ArrayList<>();
        assertThrows(NonEmptyListRequiredException.class, () -> {
            transferEngine.retrieveTransferAccountIDSet(transfers, Transfer::getFromAccountID);
        });
    }

    private TransferEntity createMockTransfer(Long id,
                                              boolean isUserTransfer,
                                              BigDecimal amount,
                                              String description,
                                              int fromUserID,
                                              int toUserID,
                                              int toAccountID,
                                              int fromAccountID){
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setTransferID(id);
        transferEntity.setUserTransfer(isUserTransfer);
        transferEntity.setTransferAmount(amount);
        transferEntity.setDescription(description);
        transferEntity.setFromUser(UserEntity.builder().userID(fromUserID).build());
        transferEntity.setToUser(UserEntity.builder().userID(toUserID).build());
        transferEntity.setFromAccount(AccountEntity.builder().acctID(fromAccountID).build());
        transferEntity.setToAccount(AccountEntity.builder().acctID(toAccountID).build());
        transferEntity.setDateTransferred(LocalDate.now());
        transferEntity.setScheduledTime(LocalTime.now());
        transferEntity.setScheduledDate(LocalDate.now());
        transferEntity.setStatus(TransferStatus.PENDING);
        return transferEntity;

    }


    @AfterEach
    void tearDown() {
    }
}