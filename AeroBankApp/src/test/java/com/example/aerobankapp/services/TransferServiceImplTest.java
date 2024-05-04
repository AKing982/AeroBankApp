package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserStringException;
import com.example.aerobankapp.exceptions.NoTransferEntitiesFoundException;
import com.example.aerobankapp.exceptions.NullTransferEntityFoundException;
import com.example.aerobankapp.exceptions.StatusNotFoundException;
import com.example.aerobankapp.repositories.TransferRepository;
import com.example.aerobankapp.workbench.utilities.Status;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
import com.example.aerobankapp.workbench.utilities.TransferType;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class TransferServiceImplTest {

    @InjectMocks
    private TransferServiceImpl transferService;

    @MockBean
    private TransferRepository transferRepository;

    private TransferEntity transferEntity =  createMockTransfer(1L,
            TransferType.USER_TO_USER, new BigDecimal("45.00"),
            "Checking to Savings transfer",
            1, 1, 1, 2);

    private TransferEntity userTransfer = createMockTransfer(2L, TransferType.USER_TO_USER, new BigDecimal("250.00"), "Transfer to BSmith",
            1, 2, 1, 4);

    @BeforeEach
    void setUp() {
      transferService = new TransferServiceImpl(transferRepository);
    }

    @Test
    public void testFindAllReturnsNonEmptyList(){
        TransferEntity mockTransfer = createMockTransfer(1L,
                TransferType.USER_TO_USER, new BigDecimal("45.00"),
                "Checking to Savings transfer",
                            1, 1, 1, 2);

        when(transferRepository.findAll()).thenReturn(List.of(mockTransfer));

        List<TransferEntity> results = transferService.findAll();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    public void testFindAllReturnsEmptyList(){
        when(transferRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NoTransferEntitiesFoundException.class, () -> {
            transferService.findAll();
        });
    }

    @Test
    public void testSaveNullTransfer(){

        when(transferRepository.save(null)).thenThrow(NullTransferEntityFoundException.class);

        assertThrows(NullTransferEntityFoundException.class, () -> {
            transferService.save(null);
        });
    }

    @Test
    public void testGetSameUserTransfer_EmptyUserString(){
        final String user = "";
        when(transferRepository.findSameUserTransfers(user)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            transferService.getSameUserTransfer(user);
        });
    }

    @Test
    public void testGetSameUserTransfer_ValidUser(){
        final String user = "AKing94";

        when(transferRepository.findSameUserTransfers(user)).thenReturn(List.of(transferEntity));

        List<TransferEntity> results = transferService.getSameUserTransfer(user);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    public void testGetSameUserTransfer_ValidUser_EmptyList(){
        final String user = "AKing94";

        when(transferRepository.findSameUserTransfers(user)).thenReturn(Collections.emptyList());

        assertThrows(NoTransferEntitiesFoundException.class, () -> {
            transferService.getSameUserTransfer(user);
        });
    }

    @Test
    public void testGetTransfersFromOUserToTUser_InvalidOriginUser(){
        final String originUser = "";
        final String targetUser = "AKing94";

        when(transferRepository.findTransfersWithOriginUserAndTargetUser(originUser, targetUser)).thenThrow(InvalidUserStringException.class);

        assertThrows(InvalidUserStringException.class, () -> {
            transferService.getTransfersFromOriginUserToTargetUser(originUser, targetUser);
        });
    }

    @Test
    public void testGetTransfersFromUserToUser_InvalidTargetUser_ValidOriginUser(){
        final String originUser = "AKing94";
        final String targetUser = "";

        when(transferRepository.findTransfersWithOriginUserAndTargetUser(originUser, targetUser)).thenThrow(InvalidUserStringException.class);

        assertThrows(InvalidUserStringException.class, () -> {
            transferService.getTransfersFromOriginUserToTargetUser(originUser, targetUser);
        });
    }

    @Test
    public void testGetTransfersFromUserToUser_InvalidOriginAndTargetUsers(){
        final String originUser = "";
        final String targetUser = "";

        when(transferRepository.findTransfersWithOriginUserAndTargetUser(originUser, targetUser)).thenThrow(InvalidUserStringException.class);

        assertThrows(InvalidUserStringException.class, () -> {
            transferService.getTransfersFromOriginUserToTargetUser(originUser, targetUser);
        });
    }

    @Test
    public void testGetTransfersFromUserToUser_ValidOriginUser_ValidTargetUser(){
        final String originUser = "AKing94";
        final String targetUser = "BSmith23";

        when(transferRepository.findTransfersWithOriginUserAndTargetUser(originUser, targetUser)).thenReturn(List.of(userTransfer));

        List<TransferEntity> result = transferService.getTransfersFromOriginUserToTargetUser(originUser, targetUser);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(transferRepository).findTransfersWithOriginUserAndTargetUser(originUser, targetUser);
    }

    @Test
    public void testGetTransfersFromUserToUser_ValidOriginUser_ValidTargetUser_ReturnNoSuchEntities(){
        final String originUser = "AKing94";
        final String targetUser = "BSmith23";

        when(transferRepository.findTransfersWithOriginUserAndTargetUser(originUser, targetUser)).thenReturn(Collections.emptyList());

        assertThrows(NoTransferEntitiesFoundException.class, () -> {
            transferService.getTransfersFromOriginUserToTargetUser(originUser, targetUser);
        });

        verify(transferRepository).findTransfersWithOriginUserAndTargetUser(originUser, targetUser);
    }

    @Test
    public void testGetTransfersFromUserToUser_OriginUserAndTargetUserWithWhiteSpace(){
        final String originUser = " AKing94 ";
        final String targetUser = " BSmith23 ";

        when(transferRepository.findTransfersWithOriginUserAndTargetUser(originUser, targetUser)).thenReturn(List.of(transferEntity));

        List<TransferEntity> results = transferService.getTransfersFromOriginUserToTargetUser(originUser, targetUser);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1 , results.size());

        verify(transferRepository).findTransfersWithOriginUserAndTargetUser(originUser, targetUser);
    }

    @Test
    public void testGetTransfersFromUserToUser_OriginUserAndTargetUserWithSpecialCharacters(){
        final String originUser = "%AKing94%";
        final String targetUser = "%BSmith23%";

        when(transferRepository.findTransfersWithOriginUserAndTargetUser(originUser, targetUser)).thenReturn(List.of(transferEntity));

        List<TransferEntity> results = transferService.getTransfersFromOriginUserToTargetUser(originUser, targetUser);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());

        verify(transferRepository).findTransfersWithOriginUserAndTargetUser(originUser, targetUser);
    }

    @Test
    public void testGetTransfersByStatus_NullStatus(){
        TransferStatus status = null;

        when(transferRepository.findTransfersByStatus(status)).thenThrow(StatusNotFoundException.class);

        assertThrows(StatusNotFoundException.class, () -> {
            transferService.getTransfersByStatus(null);
        });
    }

    @Test
    public void testGetTransfersByStatus_ValidStatus(){
        TransferStatus pending = TransferStatus.PENDING;

        when(transferRepository.findTransfersByStatus(pending)).thenReturn(List.of(transferEntity));

        List<TransferEntity> result = transferService.getTransfersByStatus(pending);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testGetTransfersByStatus_ValidStatus_ReturnEmptyTransfers(){
        TransferStatus status = TransferStatus.PENDING;

        when(transferRepository.findTransfersByStatus(status)).thenReturn(Collections.emptyList());

        assertThrows(NoTransferEntitiesFoundException.class, () -> {
            transferService.getTransfersByStatus(status);
       });
    }

    @Test
    public void testGetSameUserTransferByAccount_InvalidAccountID(){
        final int acctID = -1;


    }


    private TransferEntity createMockTransfer(Long id,
                                              TransferType transferType,
                                              BigDecimal amount,
                                              String description,
                                              int fromUserID,
                                              int toUserID,
                                              int toAccountID,
                                              int fromAccountID){
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setTransferID(id);
        transferEntity.setTransferType(transferType);
        transferEntity.setAmount(amount);
        transferEntity.setDescription(description);
        transferEntity.setFromUser(UserEntity.builder().userID(fromUserID).build());
        transferEntity.setToUser(UserEntity.builder().userID(toUserID).build());
        transferEntity.setFromAccount(AccountEntity.builder().acctID(fromAccountID).build());
        transferEntity.setToAccount(AccountEntity.builder().acctID(toAccountID).build());
        transferEntity.setScheduledTime(LocalTime.now());
        transferEntity.setScheduledDate(LocalDate.now());
        transferEntity.setStatus(TransactionStatus.PENDING);
        return transferEntity;

    }

    @AfterEach
    void tearDown() {
    }
}