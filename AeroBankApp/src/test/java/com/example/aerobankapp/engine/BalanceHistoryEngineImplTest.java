package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.TransactionDetailEntity;
import com.example.aerobankapp.model.TransactionDetail;
import com.example.aerobankapp.services.TransactionDetailService;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(JUnit38ClassRunner.class)
class BalanceHistoryEngineImplTest
{

    private BalanceHistoryEngine balanceHistoryEngine;

    @Autowired
    private TransactionDetailService transactionDetailService;

    @BeforeEach
    void setUp()
    {

    }



    @Test
    public void testGetTransactionDetails(){

    }



    @AfterEach
    void tearDown() {
    }
}