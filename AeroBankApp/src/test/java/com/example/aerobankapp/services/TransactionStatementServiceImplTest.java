package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionStatementEntity;
import com.example.aerobankapp.repositories.TransactionStatementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class TransactionStatementServiceImplTest {

    @InjectMocks
    private TransactionStatementServiceImpl transactionStatementService;

    @Autowired
    private TransactionStatementRepository transactionStatementRepository;

    @BeforeEach
    void setUp() {
        transactionStatementService = new TransactionStatementServiceImpl(transactionStatementRepository);
    }

    @Test
    public void testGetTransactionStatementsByAcctID_Valid_AcctID(){

        List<TransactionStatementEntity> transactionStatementEntityList = transactionStatementService.getTransactionStatementsByAcctID(1);


        assertNotNull(transactionStatementEntityList);
        assertEquals(4, transactionStatementEntityList.size());
    }

    @Test
    public void getPendingTransactionByAcctID(){

    }

    @AfterEach
    void tearDown() {
    }
}