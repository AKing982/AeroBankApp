package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.DepositQueueService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositEngineTest {

    private DepositEngine depositEngine;

    @Autowired
    private CalculationEngine calculationEngine;

    @Autowired
    private DepositQueueService depositQueueService;

    private DepositQueue depositQueue;

    private DepositDTO depositDTO;

    private DepositDTO depositDTO2;


    @BeforeEach
    void setUp()
    {

        depositDTO = DepositDTO.builder()
                .depositID(1)
                .amount(new BigDecimal("45.00"))
                .timeScheduled(LocalDateTime.now())
                .date(LocalDate.now())
                .description("Transfer 1")
                .accountCode("A1")
                .userID(1)
                .scheduleInterval(ScheduleType.ONCE)
                .build();

         depositDTO2 = DepositDTO.builder()
                .depositID(2)
                .description("Transfer 2")
                .amount(new BigDecimal("1214"))
                .timeScheduled(LocalDateTime.of(2024, 8, 5, 3, 3))
                .date(LocalDate.now())
                .accountCode("A3")
                .scheduleInterval(ScheduleType.ONCE)
                .userID(1)
                .build();

        depositQueue = new DepositQueue(depositQueueService);

        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO2);

        depositEngine = new DepositEngine(depositQueue, calculationEngine);
    }

    @Test
    public void testProcessingDepositsInQueue()
    {
       List<DepositDTO> actualDeposits = depositEngine.processDepositsInQueue();

       assertEquals(2, actualDeposits.size());
       assertTrue(actualDeposits.contains(depositDTO));
       assertTrue(actualDeposits.contains(depositDTO2));
    }

    @Test
    public void testProcessDeposits()
    {
        List<DepositDTO> depositDTOList = depositEngine.processDeposits();

        assertEquals(1, depositDTOList.size());
        assertTrue(depositDTOList.contains(depositDTO));
        assertTrue(depositDTOList.contains(depositDTO2));
    }



    @AfterEach
    void tearDown() {
    }
}