package com.example.aerobankapp.scheduler.jobdetail;

import com.example.aerobankapp.workbench.transactions.Purchase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
class PurchaseJobDetailTest
{
    @MockBean
    private PurchaseJobDetail purchaseJobDetail;

    @Autowired
    @Mock
    private Purchase purchase;

    @BeforeEach
    void setUp()
    {
        purchase = mock(Purchase.class);
        purchaseJobDetail = new PurchaseJobDetail(purchase);
    }

    @Test
    public void initialize()
    {
        assertNotNull(purchaseJobDetail);
        assertNotNull(purchase);
    }

    @AfterEach
    void tearDown() {
    }
}