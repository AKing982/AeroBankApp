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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(JUnit38ClassRunner.class)
@Transactional
class DepositEngineTest {

    @InjectMocks
    private DepositEngine depositEngine;

    @Autowired
    private CalculationEngine calculationEngine;

    @Autowired
    private DepositQueueService depositQueueService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NotificationService notificationService;

    @Mock
    private DepositQueue depositQueue;

    private DepositDTO depositDTO;

    private DepositDTO depositDTO2;


    @BeforeEach
    void setUp() {

        depositDTO = DepositDTO.builder()
                .depositID(1)
                .amount(new BigDecimal("45.00"))
                .timeScheduled(LocalTime.now())
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
                .timeScheduled(LocalTime.of(8, 15))
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
    public void testProcessingDepositsInQueue() {
        List<DepositDTO> actualDeposits = depositEngine.getDepositsFromQueue();

        assertEquals(2, actualDeposits.size());
        assertTrue(actualDeposits.contains(depositDTO));
        assertTrue(actualDeposits.contains(depositDTO2));
    }

    @Test
    public void testProcessDeposits() {
        List<ProcessedDepositDTO> depositDTOList = depositEngine.process();
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
    public void testProcessingNullDeposits() {
        DepositDTO nullDeposit1 = null;
        DepositDTO nullDeposit2 = null;
        depositQueue.add(nullDeposit1);
        depositQueue.add(nullDeposit2);

        assertThrows(NullPointerException.class, () -> {
            depositEngine.process();
        });
    }

    @Test
    public void testProcessingDepositWithNoAccountCode() {
        DepositDTO depositDTO4 = DepositDTO.builder()
                .depositID(1)
                .accountCode(null)
                .timeScheduled(LocalTime.now())
                .scheduleInterval(ScheduleType.ONCE)
                .date(LocalDate.now())
                .amount(new BigDecimal("45.00"))
                .description("Transfer 1")
                .userID(1)
                .build();

        depositQueue.add(depositDTO4);

        assertThrows(NullPointerException.class, () -> {
            depositEngine.process();
        });

    }

    @Test
    public void testNullAmountDepositProcessing() {
        DepositDTO depositDTO4 = DepositDTO.builder()
                .depositID(1)
                .accountCode("A1")
                .timeScheduled(LocalTime.now())
                .scheduleInterval(ScheduleType.ONCE)
                .date(LocalDate.now())
                .amount(null)
                .description("Transfer 1")
                .userID(1)
                .accountID(1)
                .build();

        depositQueue.add(depositDTO4);

        assertThrows(NullPointerException.class, () -> {
            depositEngine.process();
        });
    }

    @Test
    public void testNotifyAccountHolder() {

    }

    @Test
    public void testGetProcessedBalances() {
        List<ProcessedDepositDTO> depositDTOList = depositEngine.process();
        Map<Integer, BigDecimal> balanceEntryMap = depositEngine.getProcessedBalances(depositDTOList);

        Map<Integer, BigDecimal> balanceMap = new HashMap<>();
        balanceMap.put(1, new BigDecimal("4545.000"));
        balanceMap.put(3, new BigDecimal("9014.000"));

        assertEquals(balanceMap, balanceEntryMap);
        assertEquals(balanceMap.size(), balanceEntryMap.size());
    }

    @Test
    public void testGetProcessedBalancesWithEmptyList() {
        List<ProcessedDepositDTO> emptyDeposits = new ArrayList<>();
        Map<Integer, BigDecimal> balanceEntryMap = depositEngine.getProcessedBalances(emptyDeposits);

        assertEquals(0, balanceEntryMap.size());
        assertTrue(balanceEntryMap.isEmpty());
    }

    @Test
    public void testGetProcessedBalancesWithNoAccountIDAndBalance() {
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
    public void testGetProcessedBalancesWithNullDeposit() {
        List<ProcessedDepositDTO> depositDTOList = new ArrayList<>();
        depositDTOList.add(null);

        Map<Integer, BigDecimal> balanceMap = depositEngine.getProcessedBalances(depositDTOList);

        assertEquals(1, balanceMap.size());
    }

    @Test
    public void testIndividualDeposits_Success() {
        DepositDTO mockDeposit = DepositDTO.builder()
                .depositID(1)
                .scheduleInterval(ScheduleType.ONCE)
                .date(LocalDate.now())
                .accountID(1)
                .accountCode("A1")
                .amount(new BigDecimal("120.00"))
                .description("Transfer")
                .timeScheduled(LocalTime.now())
                .userID(1)
                .nonUSDCurrency(false)
                .build();

        ProcessedDepositDTO processedDepositDTO = buildProcessedDeposit(mockDeposit, new BigDecimal("4620.000"));
        ProcessedDepositDTO actualProcessedDeposit = depositEngine.processIndividualDeposit(mockDeposit);

        assertEquals(processedDepositDTO, actualProcessedDeposit);
        assertEquals(processedDepositDTO.depositID(), actualProcessedDeposit.depositID());
        assertNotNull(actualProcessedDeposit);

    }

    @Test
    public void testGetProcessedDeposits_NullList() {
        List<DepositDTO> depositDTOList = null;

        assertThrows(NullPointerException.class, () -> {
            List<ProcessedDepositDTO> processedDepositDTOS = depositEngine.getProcessedDeposits(depositDTOList);
        });
    }

    @Test
    public void testGetProcessedDeposits_EmptyList() {
        List<DepositDTO> emptyDepositList = new ArrayList<>();
        List<ProcessedDepositDTO> processedDepositDTOS = depositEngine.getProcessedDeposits(emptyDepositList);

        assertEquals(emptyDepositList, processedDepositDTOS);
        assertEquals(emptyDepositList.size(), processedDepositDTOS.size());
    }

    @Test
    public void testGetProcessedDeposits() {
        DepositDTO mockDeposit = createMockDeposit(1, 1, 1, "A1", new BigDecimal("120.00"), "Transfer 1");
        DepositDTO mockDeposit2 = createMockDeposit(2, 1, 1, "A1", new BigDecimal("56.00"), "Transfer 2");
        List<DepositDTO> depositDTOList = Arrays.asList(mockDeposit, mockDeposit2);
        List<ProcessedDepositDTO> processedDepositDTOS = depositEngine.getProcessedDeposits(depositDTOList);

        assertEquals(depositDTOList.size(), processedDepositDTOS.size());
        for (int i = 0; i < depositDTOList.size(); i++) {
            DepositDTO original = depositDTOList.get(i);
            ProcessedDepositDTO processed = processedDepositDTOS.get(i);

            assertEquals(original.getDepositID(), processed.depositID());
            assertEquals(original.getAccountID(), processed.accountID());
            assertEquals(original.getUserID(), processed.userID());
            assertEquals(original.getAmount(), processed.amount());
            // Add more assertions for other fields if necessary
        }
    }

    @Test
    public void testProcessIndividualDeposit_NullDepositDTO() {
        ProcessedDepositDTO expected = null;
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessedDepositDTO processedDepositDTO = depositEngine.processIndividualDeposit(null);
        });
    }

    @Test
    public void testProcessIndividualDeposit_NullAccountCode() {
        DepositDTO mockDeposit = createMockDeposit(1, 1, 1, null, new BigDecimal("1215"), "Transfer");
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessedDepositDTO processedDepositDTO = depositEngine.processIndividualDeposit(mockDeposit);
        });
    }

    @Test
    public void testProcessIndividualDeposit_InvalidUserID() {
        DepositDTO mockDeposit = createMockDeposit(1, -1, 1, "A1", new BigDecimal("45.00"), "Transfer");
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessedDepositDTO processedDepositDTO = depositEngine.processIndividualDeposit(mockDeposit);
        });
    }

    @Test
    public void testProcessIndividualDeposit_InvalidAmount() {
        DepositDTO mockDeposit = createMockDeposit(1, 1, 1, "A1", null, "Transfer");
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessedDepositDTO processedDepositDTO = depositEngine.processIndividualDeposit(mockDeposit);
        });
    }

    @Test
    public void testProcessIndividualDeposit_InvalidCriteria() {
        DepositDTO mockDeposit = createMockDeposit(1, -1, 1, null, null, "Transfer");;
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessedDepositDTO processedDepositDTO = depositEngine.processIndividualDeposit(mockDeposit);
        });
    }

    @Test
    public void testProcessAndUpdateDeposits_Normal() {
        // Arrange
        DepositDTO mockDeposit1 = createMockDeposit(1, 1, 1, "A1", new BigDecimal("1215"), "Transfer 1");
        DepositDTO mockDeposit2 = createMockDeposit(1, 1, 2, "A1", new BigDecimal("45.00"), "Transfer 2");
        List<DepositDTO> depositDTOList = Arrays.asList(mockDeposit1, mockDeposit2);
        ProcessedDepositDTO mockProcessedDeposit = createMockProcessedDeposit(1, 1, 1, "A1", new BigDecimal("1150"), new BigDecimal("45.00"), "Transfer 1");
        ProcessedDepositDTO mockProcessedDeposit2 = createMockProcessedDeposit(1, 1, 2, "A1", new BigDecimal("1100"), new BigDecimal("50.00"), "Transfer 2");
        List<ProcessedDepositDTO> processedDepositDTOS = Arrays.asList(mockProcessedDeposit, mockProcessedDeposit2);
        Map<Integer, BigDecimal> mockProcessedBalances = new HashMap<>();
        mockProcessedBalances.put(1, new BigDecimal("1150"));
        mockProcessedBalances.put(2, new BigDecimal("1100"));


        when(depositQueue.getAllElements()).thenReturn(depositDTOList);
        when(depositEngine.getProcessedDeposits(depositDTOList)).thenReturn(processedDepositDTOS);
        when(depositEngine.getProcessedBalances(processedDepositDTOS)).thenReturn(mockProcessedBalances);

        // Act
        depositEngine.processAndUpdateDeposits();

        // Assert
        verify(depositQueue).getAllElements();
        verify(depositEngine).getProcessedDeposits(depositDTOList);
        verify(depositEngine).getProcessedBalances(processedDepositDTOS);
        verify(depositEngine).updateAccountBalances(mockProcessedBalances);
    }

    @Test
    public void testSendDepositNotification()
    {

    }

    private ProcessedDepositDTO createMockProcessedDeposit(int id, int userID, int acctID, String acctCode, BigDecimal newBalance, BigDecimal amount, String description)
    {
        return ProcessedDepositDTO.builder()
                .depositID(id)
                .userID(userID)
                .accountID(acctID)
                .accountCode(acctCode)
                .description(description)
                .newBalance(newBalance)
                .amount(amount)
                .build();
    }

    private DepositDTO createMockDeposit(int id, int userID, int acctID, String acctCode, BigDecimal amount, String description) {
        return DepositDTO.builder()
                .depositID(id)
                .scheduleInterval(ScheduleType.ONCE)
                .date(LocalDate.now())
                .accountID(acctID)
                .accountCode(acctCode)
                .amount(amount)
                .description(description)
                .timeScheduled(LocalTime.now())
                .userID(userID)
                .nonUSDCurrency(false)
                .build();
    }

    private ProcessedDepositDTO buildProcessedDeposit(DepositDTO depositDTO, BigDecimal balance)
    {
        return ProcessedDepositDTO.builder()
                .accountID(depositDTO.getAccountID())
                .depositID(depositDTO.getDepositID())
                .description(depositDTO.getDescription())
                .newBalance(balance)
                .accountCode(depositDTO.getAccountCode())
                .userID(depositDTO.getUserID())
                .amount(depositDTO.getAmount())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
    }
}