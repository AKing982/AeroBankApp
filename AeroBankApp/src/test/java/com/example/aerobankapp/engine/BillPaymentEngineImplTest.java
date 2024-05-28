package com.example.aerobankapp.engine;

import com.example.aerobankapp.services.BillPaymentNotificationService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BillPaymentEngineImplTest {

    @InjectMocks
    private BillPaymentEngineImpl billPaymentEngine;

    @MockBean
    private BillPaymentScheduleService billPaymentScheduleService;

    @MockBean
    private BillPaymentService billPaymentService;

    @MockBean
    private BillPaymentNotificationService billPaymentNotificationService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        billPaymentEngine = new BillPaymentEngineImpl(rabbitTemplate, billPaymentScheduleService, billPaymentService, billPaymentNotificationService);
    }

    @Test
    public void testAutoPayBills_whenEmptyBillPayments_throwNonEmptyListException(){

    }

    @AfterEach
    void tearDown() {
    }
}