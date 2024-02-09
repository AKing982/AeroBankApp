package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.scheduler.ScheduleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    @Mock
    private CalculationEngine calculationEngine;

    @Mock
    private DepositQueue depositQueue;

    @BeforeEach
    void setUp()
    {
        depositEngine = new DepositEngine(depositQueue, calculationEngine);
        DepositDTO depositDTO = DepositDTO.builder()
                .depositID(1)
                .amount(new BigDecimal("45.00"))
                .timeScheduled(LocalDateTime.now())
                .date(LocalDate.now())
                .description("Transfer 1")
                .accountCode("A1")
                .userID(1)
                .scheduleInterval(ScheduleType.ONCE)
                .build();

        DepositDTO depositDTO1 = DepositDTO.builder()
                .depositID(2)
                .description("Transfer 2")
                .amount(new BigDecimal("1214"))
                .timeScheduled(LocalDateTime.of(2024, 1130, 5, 3, 3))
                .date(LocalDate.now())
                .accountCode("A3")
                .scheduleInterval(ScheduleType.ONCE)
                .userID(1)
                .build();

        depositQueue = mock(DepositQueue.class);

        List<DepositDTO> depositDTOS = new ArrayList<>();
        depositDTOS.add(depositDTO1);
        depositDTOS.add(depositDTO);
        depositQueue.addAll(depositDTOS);
    }

    @Test
    public void testProcessingDepositsInQueue()
    {

    }

    @AfterEach
    void tearDown() {
    }
}