package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.DepositsEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DepositServiceImplTest
{
    @Autowired
    private DepositServiceImpl depositService;


    @BeforeEach
    void setUp() {
    }

    @Test
    public void testGetDepositsByAcctID()
    {
        List<DepositsEntity> depositsEntityList = depositService.getDepositsByAcctID(1);

        assertNotNull(depositsEntityList);
        assertEquals(2, depositsEntityList.size());
    }


    @AfterEach
    void tearDown() {
    }
}