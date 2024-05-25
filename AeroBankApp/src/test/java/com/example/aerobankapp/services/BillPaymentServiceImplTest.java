package com.example.aerobankapp.services;

import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.DuplicateBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentException;
import com.example.aerobankapp.exceptions.InvalidBillPaymentParametersException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.repositories.BillPaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BillPaymentServiceImplTest {

    @Mock
    private BillPaymentService billPaymentService;

    @Mock
    private BillPaymentRepository billPaymentRepository;

    @InjectMocks
    private BillPaymentServiceImpl billPaymentServiceImpl;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindBillPaymentsByUserID_invalidUserID_shouldThrowException() throws Exception{
        int userID = -1;

        assertThrows(InvalidUserIDException.class, () -> {
            billPaymentServiceImpl.findBillPaymentsByUserID(userID);
        });
    }

    @Test
    public void testFindBillPaymentsByUserID_validUserID_shouldReturnEmptyList(){
        int userID = 1;

        when(billPaymentServiceImpl.findBillPaymentsByUserID(userID)).thenReturn(Collections.emptyList());

        List<BillPaymentEntity> result = billPaymentServiceImpl.findBillPaymentsByUserID(userID);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindBillPaymentsByUserID_validUserID_returnBillPaymentsList() throws Exception{
        int userID = 1;

        BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
        billPaymentEntity.setPaymentAmount(new BigDecimal(120));
        billPaymentEntity.setPaymentID(1L);
        billPaymentEntity.setPayeeName("Payee 1");
        billPaymentEntity.setUser(UserEntity.builder().userID(1).userCredentials(UserCredentials.builder().username("AKing94").build()).build());

        List<BillPaymentEntity> billPaymentEntities = Collections.singletonList(billPaymentEntity);
        when(billPaymentServiceImpl.findBillPaymentsByUserID(userID)).thenReturn(billPaymentEntities);
        List<BillPaymentEntity> actualBillPayments = billPaymentServiceImpl.findBillPaymentsByUserID(userID);

        assertNotNull(actualBillPayments);
        assertEquals(1, actualBillPayments.size());
        assertEquals(1L, actualBillPayments.get(0).getPaymentID());
    }

    @Test
    public void testFindBillPaymentsByUserID_edgeCaseUserID_shouldThrowException(){
        int userID = 0;

        assertThrows(InvalidUserIDException.class, () -> {
            billPaymentServiceImpl.findBillPaymentsByUserID(userID);
        });
    }

    @Test
    public void testFindBillPaymentsByUserID_edgeCaseUserID_UserIDNotInDatabase_shouldReturnEmptyList(){
        int userID = 4;

        when(billPaymentServiceImpl.findBillPaymentsByUserID(userID)).thenReturn(Collections.emptyList());

        List<BillPaymentEntity> billPaymentEntities = billPaymentServiceImpl.findBillPaymentsByUserID(userID);

        assertEquals(0, billPaymentEntities.size());
    }
    @Test
    public void testFindBillPaymentsByUserID_nullUserID_shouldThrowException() {
        Integer userID = null;

        when(billPaymentServiceImpl.findBillPaymentsByUserID(userID)).thenThrow(new InvalidUserIDException("UserID cannot be null"));

        assertThrows(InvalidUserIDException.class, () -> {
            billPaymentServiceImpl.findBillPaymentsByUserID(userID);
        });
    }

    @Test
    public void testSaveBillPaymentEntity_nullBillPayment_shouldThrowException(){
        BillPaymentEntity billPaymentEntity = null;

        assertThrows(InvalidBillPaymentException.class, () -> {
            billPaymentServiceImpl.save(billPaymentEntity);
        });

        verify(billPaymentRepository, never()).save(any(BillPaymentEntity.class));
    }

    @Test
    public void testSave_duplicateBillPayment_shouldHandleCorrectly() {
        BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
        billPaymentEntity.setPaymentID(1L);
        billPaymentEntity.setPaymentAmount(new BigDecimal("120"));
        billPaymentEntity.setUser(UserEntity.builder()
                .userCredentials(UserCredentials.builder().username("AKing94").build())
                .userID(1)
                .build());
        billPaymentEntity.setPayeeName("Payee 1");

        lenient().when(billPaymentRepository.existsById(billPaymentEntity.getPaymentID())).thenThrow(new DuplicateBillPaymentException("Found existing bill payment"));

        verify(billPaymentRepository, never()).save(any(BillPaymentEntity.class));
    }

    @Test
    public void testSave_nullBillPaymentParameters_shouldThrowException(){
        BillPaymentEntity billPaymentEntity = new BillPaymentEntity();
        billPaymentEntity.setPaymentSchedule(null);
        billPaymentEntity.setPaymentType(null);
        billPaymentEntity.setAccount(null);
        billPaymentEntity.setUser(null);
        billPaymentEntity.setPaymentAmount(null);
        billPaymentEntity.setPayeeName(null);
        billPaymentEntity.setPaymentID(1L);

        assertThrows(InvalidBillPaymentParametersException.class, () -> {
            billPaymentServiceImpl.save(billPaymentEntity);
        });


        verify(billPaymentRepository, never()).save(billPaymentEntity);
    }




    @AfterEach
    void tearDown() {
    }
}