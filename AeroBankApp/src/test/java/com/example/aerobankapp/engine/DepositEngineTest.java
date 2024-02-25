package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.dto.ProcessedDepositDTO;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.DepositQueueService;
import com.example.aerobankapp.services.DepositService;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.workbench.transactions.Deposit;
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
    private DepositService depositService;

    @Autowired
    private NotificationService notificationService;

    @Mock
    private DepositQueue depositQueue;

    private Deposit deposit;

    private Deposit deposit2;


    @BeforeEach
    void setUp() {

        deposit = new Deposit();
        deposit.setDepositID(1);
        deposit.setUserID(1);
        deposit.setAcctCode("A1");
        deposit.setAccountID(1);
        deposit.setTimeScheduled(LocalTime.now());
        deposit.setAmount(new BigDecimal("45.00"));
        deposit.setDescription("Transfer 1");
        deposit.setScheduleInterval(ScheduleType.ONCE);
        deposit.setDateScheduled(LocalDate.now());

        deposit2 = new Deposit();
        deposit2.setUserID(1);
        deposit2.setDescription("Transfer 2");
        deposit2.setDepositID(1);
        deposit2.setAmount(new BigDecimal("1214"));
        deposit2.setScheduleInterval(ScheduleType.ONCE);
        deposit2.setDateScheduled(LocalDate.now());
        deposit2.setAcctCode("A2");
        deposit2.setTimeScheduled(LocalTime.of(8, 15));
        deposit2.setAccountID(3);

        depositQueue = new DepositQueue(depositQueueService);

        depositQueue.add(deposit);
        depositQueue.add(deposit2);

        depositEngine = new DepositEngine(depositQueue, calculationEngine, accountService, depositService, notificationService);
    }

    @Test
    public void testProcessingDepositsInQueue() {
        List<Deposit> actualDeposits = depositEngine.getDepositsFromQueue();

        assertEquals(2, actualDeposits.size());
        assertTrue(actualDeposits.contains(deposit));
        assertTrue(actualDeposits.contains(deposit2));
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
        Deposit nullDeposit1 = null;
        Deposit nullDeposit2 = null;
        depositQueue.add(nullDeposit1);
        depositQueue.add(nullDeposit2);

        assertThrows(NullPointerException.class, () -> {
            depositEngine.process();
        });
    }

    @Test
    public void testProcessingDepositWithNoAccountCode() {
        Deposit depositDTO4 = new Deposit();
        depositDTO4.setAccountID(1);
        depositDTO4.setDepositID(1);
        depositDTO4.setAcctCode(null);
        depositDTO4.setAmount(new BigDecimal("45.00"));
        depositDTO4.setTimeScheduled(LocalTime.now());
        depositDTO4.setUserID(1);
        depositDTO4.setDescription("Transfer 1");
        depositDTO4.setScheduleInterval(ScheduleType.ONCE);
        depositDTO4.setDateScheduled(LocalDate.now());

        depositQueue.add(depositDTO4);

        assertThrows(NullPointerException.class, () -> {
            depositEngine.process();
        });

    }

    @Test
    public void testNullAmountDepositProcessing() {
        Deposit depositDTO4 = new Deposit();
        depositDTO4.setAccountID(1);
        depositDTO4.setDepositID(1);
        depositDTO4.setAcctCode("A1");
        depositDTO4.setAmount(null);
        depositDTO4.setTimeScheduled(LocalTime.now());
        depositDTO4.setUserID(1);
        depositDTO4.setDescription("Transfer 1");
        depositDTO4.setScheduleInterval(ScheduleType.ONCE);
        depositDTO4.setDateScheduled(LocalDate.now());

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
        Deposit depositDTO4 = new Deposit();
        depositDTO4.setAccountID(1);
        depositDTO4.setDepositID(1);
        depositDTO4.setAcctCode(null);
        depositDTO4.setAmount(new BigDecimal("45.00"));
        depositDTO4.setTimeScheduled(LocalTime.now());
        depositDTO4.setUserID(1);
        depositDTO4.setDescription("Transfer 1");
        depositDTO4.setScheduleInterval(ScheduleType.ONCE);
        depositDTO4.setDateScheduled(LocalDate.now());

        ProcessedDepositDTO processedDepositDTO = buildProcessedDeposit(depositDTO4, new BigDecimal("4620.000"));
        ProcessedDepositDTO actualProcessedDeposit = depositEngine.processIndividualDeposit(deposit);

        assertEquals(processedDepositDTO, actualProcessedDeposit);
        assertEquals(processedDepositDTO.depositID(), actualProcessedDeposit.depositID());
        assertNotNull(actualProcessedDeposit);

    }

    @Test
    public void testGetProcessedDeposits_NullList() {
        List<Deposit> depositDTOList = null;

        assertThrows(NullPointerException.class, () -> {
            List<ProcessedDepositDTO> processedDepositDTOS = depositEngine.getProcessedDeposits(depositDTOList);
        });
    }

    @Test
    public void testGetProcessedDeposits_EmptyList() {
        List<Deposit> emptyDepositList = new ArrayList<>();
        List<ProcessedDepositDTO> processedDepositDTOS = depositEngine.getProcessedDeposits(emptyDepositList);

        assertEquals(emptyDepositList, processedDepositDTOS);
        assertEquals(emptyDepositList.size(), processedDepositDTOS.size());
    }

    @Test
    public void testGetProcessedDeposits() {
        Deposit mockDeposit = createMockDeposit(1, 1, 1, "A1", new BigDecimal("120.00"), "Transfer 1");
        Deposit mockDeposit2 = createMockDeposit(2, 1, 1, "A1", new BigDecimal("56.00"), "Transfer 2");
        List<Deposit> depositDTOList = Arrays.asList(mockDeposit, mockDeposit2);
        List<ProcessedDepositDTO> processedDepositDTOS = depositEngine.getProcessedDeposits(depositDTOList);

        assertEquals(depositDTOList.size(), processedDepositDTOS.size());
        for (int i = 0; i < depositDTOList.size(); i++) {
            Deposit original = depositDTOList.get(i);
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
        Deposit mockDeposit = createMockDeposit(1, 1, 1, null, new BigDecimal("1215"), "Transfer");
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessedDepositDTO processedDepositDTO = depositEngine.processIndividualDeposit(mockDeposit);
        });
    }

    @Test
    public void testProcessIndividualDeposit_InvalidUserID() {
        Deposit mockDeposit = createMockDeposit(1, -1, 1, "A1", new BigDecimal("45.00"), "Transfer");
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessedDepositDTO processedDepositDTO = depositEngine.processIndividualDeposit(mockDeposit);
        });
    }

    @Test
    public void testProcessIndividualDeposit_InvalidAmount() {
        Deposit mockDeposit = createMockDeposit(1, 1, 1, "A1", null, "Transfer");
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessedDepositDTO processedDepositDTO = depositEngine.processIndividualDeposit(mockDeposit);
        });
    }

    @Test
    public void testProcessIndividualDeposit_InvalidCriteria() {
        Deposit mockDeposit = createMockDeposit(1, -1, 1, null, null, "Transfer");;
        assertThrows(IllegalArgumentException.class, () -> {
            ProcessedDepositDTO processedDepositDTO = depositEngine.processIndividualDeposit(mockDeposit);
        });
    }

    @Test
    public void testProcessAndUpdateDeposits_Normal() {
        // Arrange
        Deposit mockDeposit1 = createMockDeposit(1, 1, 1, "A1", new BigDecimal("1215"), "Transfer 1");
        Deposit mockDeposit2 = createMockDeposit(1, 1, 2, "A1", new BigDecimal("45.00"), "Transfer 2");
        List<Deposit> depositDTOList = Arrays.asList(mockDeposit1, mockDeposit2);
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

    private Deposit createMockDeposit(int id, int userID, int acctID, String acctCode, BigDecimal amount, String description) {

        Deposit depositDTO4 = new Deposit();
        depositDTO4.setAccountID(acctID);
        depositDTO4.setDepositID(id);
        depositDTO4.setAcctCode(acctCode);
        depositDTO4.setAmount(amount);
        depositDTO4.setTimeScheduled(LocalTime.now());
        depositDTO4.setUserID(userID);
        depositDTO4.setDescription(description);
        depositDTO4.setScheduleInterval(ScheduleType.ONCE);
        depositDTO4.setDateScheduled(LocalDate.now());

        return depositDTO4;
    }

    private ProcessedDepositDTO buildProcessedDeposit(Deposit depositDTO, BigDecimal balance)
    {
        return ProcessedDepositDTO.builder()
                .accountID(depositDTO.getAccountID())
                .depositID(depositDTO.getDepositID())
                .description(depositDTO.getDescription())
                .newBalance(balance)
                .accountCode(depositDTO.getAcctCode())
                .userID(depositDTO.getUserID())
                .amount(depositDTO.getAmount())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
    }
}