package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.dto.ProcessedDepositDTO;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.DepositQueueService;
import com.example.aerobankapp.services.NotificationService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(JUnit38ClassRunner.class)
@Transactional
class DepositEngineTest {

    private DepositEngine depositEngine;

    @Autowired
    private CalculationEngine calculationEngine;

    @Autowired
    private DepositQueueService depositQueueService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NotificationService notificationService;

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
                .accountID(1)
                .scheduleInterval(ScheduleType.ONCE)
                .build();

         depositDTO2 = DepositDTO.builder()
                .depositID(2)
                .description("Transfer 2")
                .amount(new BigDecimal("1214"))
                .timeScheduled(LocalDateTime.of(2024, 8, 5, 3, 3))
                .date(LocalDate.now())
                .accountCode("A2")
                .scheduleInterval(ScheduleType.ONCE)
                .userID(1)
                 .accountID(3)
                .build();

        depositQueue = new DepositQueue(depositQueueService);

        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO2);

        depositEngine = new DepositEngine(depositQueue, calculationEngine, accountService, notificationService);
    }

    @Test
    public void testProcessingDepositsInQueue()
    {
       List<DepositDTO> actualDeposits = depositEngine.getDepositsFromQueue();

       assertEquals(2, actualDeposits.size());
       assertTrue(actualDeposits.contains(depositDTO));
       assertTrue(actualDeposits.contains(depositDTO2));
    }

    @Test
    public void testProcessDeposits()
    {
        List<ProcessedDepositDTO> depositDTOList = depositEngine.processDeposits();
        Map<Integer, BigDecimal> accountCodeBalanceMap = depositEngine.getProcessedBalances(depositDTOList);
        ProcessedDepositDTO processedDepositDTO = ProcessedDepositDTO.builder()
                .depositID(1)
                .newBalance(new BigDecimal("1250.000"))
                .createdAt(LocalDateTime.now())
                .accountID(1)
                .userID(1)
                .description("Transfer 1")
                .amount(new BigDecimal("45.00"))
                .accountCode("A1")
                .build();

        Map<Integer, BigDecimal> expectedMap = new HashMap<>();
        expectedMap.put(1, new BigDecimal("4545.000"));
        expectedMap.put(3, new BigDecimal("9014.000"));

      //  assertEquals(2, depositDTOList.);
        assertEquals(expectedMap, accountCodeBalanceMap);
        assertEquals(expectedMap.size(), accountCodeBalanceMap.size());
        assertEquals(processedDepositDTO.depositID(), depositDTOList.get(0).depositID());
       // assertTrue(depositDTOList.contains(processedDepositDTO));
       // assertTrue(depositDTOList.contains(depositDTO2));
    }

    @Test
    public void testProcessingNullDeposits()
    {
        DepositDTO nullDeposit1 = null;
        DepositDTO nullDeposit2 = null;
        depositQueue.add(nullDeposit1);
        depositQueue.add(nullDeposit2);

        assertThrows(NullPointerException.class, () -> {
            depositEngine.processDeposits();
        });
    }

    @Test
    public void testProcessingDepositWithNoAccountCode()
    {
        DepositDTO depositDTO4 = DepositDTO.builder()
                .depositID(1)
                .accountCode(null)
                .timeScheduled(LocalDateTime.now())
                .scheduleInterval(ScheduleType.ONCE)
                .date(LocalDate.now())
                .amount(new BigDecimal("45.00"))
                .description("Transfer 1")
                .userID(1)
                .build();

        depositQueue.add(depositDTO4);

      assertThrows(NullPointerException.class,()-> {
          depositEngine.processDeposits();
      });

    }

    @Test
    public void testNullAmountDepositProcessing()
    {
        DepositDTO depositDTO4 = DepositDTO.builder()
                .depositID(1)
                .accountCode("A1")
                .timeScheduled(LocalDateTime.now())
                .scheduleInterval(ScheduleType.ONCE)
                .date(LocalDate.now())
                .amount(null)
                .description("Transfer 1")
                .userID(1)
                .accountID(1)
                .build();

        depositQueue.add(depositDTO4);

        assertThrows(NullPointerException.class, () -> {
            depositEngine.processDeposits();
        });
    }

    @Test
    public void testNotifyAccountHolder()
    {

    }

    @Test
    public void testGetProcessedBalances()
    {
        List<ProcessedDepositDTO> depositDTOList = depositEngine.processDeposits();
        Map<Integer, BigDecimal> balanceEntryMap = depositEngine.getProcessedBalances(depositDTOList);

        Map<Integer, BigDecimal> balanceMap = new HashMap<>();
        balanceMap.put(1, new BigDecimal("4545.000"));
        balanceMap.put(3, new BigDecimal("9014.000"));

        assertEquals(balanceMap, balanceEntryMap);
        assertEquals(balanceMap.size(), balanceEntryMap.size());
    }

    @Test
    public void testGetProcessedBalancesWithEmptyList()
    {
        List<ProcessedDepositDTO> emptyDeposits = new ArrayList<>();
        Map<Integer, BigDecimal> balanceEntryMap = depositEngine.getProcessedBalances(emptyDeposits);

        assertEquals(0, balanceEntryMap.size());
        assertTrue(balanceEntryMap.isEmpty());
    }

    @Test
    public void testGetProcessedBalancesWithNoAccountIDAndBalance()
    {
        ProcessedDepositDTO depositDTO5 = ProcessedDepositDTO.builder()
                .depositID(1)
                .description("Transfer 1")
                .accountCode("A1")
                .amount(new BigDecimal("45.02"))
                .userID(1)
                .build();

        List<ProcessedDepositDTO> depositDTOList = new ArrayList<>();
        depositDTOList.add(depositDTO5);

        assertThrows(IllegalArgumentException.class, () -> {
            Map<Integer, BigDecimal> processedDepositDTOS = depositEngine.getProcessedBalances(depositDTOList);

        });
    }

    @Test
    public void testGetProcessedBalancesWithNullDeposit()
    {
        List<ProcessedDepositDTO> depositDTOList = new ArrayList<>();
        depositDTOList.add(null);

        Map<Integer, BigDecimal> balanceMap = depositEngine.getProcessedBalances(depositDTOList);

        assertEquals(1, balanceMap.size());
    }

    @AfterEach
    void tearDown() {
    }
}