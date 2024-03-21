package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.NoTransferEntitiesFoundException;
import com.example.aerobankapp.exceptions.NullTransferEntityFoundException;
import com.example.aerobankapp.repositories.TransferRepository;
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
            false, new BigDecimal("45.00"),
            "Checking to Savings transfer",
            1, 1, 1, 2);

    @BeforeEach
    void setUp() {
      transferService = new TransferServiceImpl(transferRepository);
    }

    @Test
    public void testFindAllReturnsNonEmptyList(){
        TransferEntity mockTransfer = createMockTransfer(1L,
                false, new BigDecimal("45.00"),
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
    public void testSaveValidEntity(){
        when(transferRepository.save(transferEntity)).thenAnswer(invocation -> invocation.getArgument(0));

        //transferService.save(transferEntity);

        verify(transferRepository).save(transferEntity);
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
        return transferEntity;

    }

    @AfterEach
    void tearDown() {
    }
}