package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.DepositService;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositProcessorImplTest
{

    @InjectMocks
    private DepositProcessorImpl depositProcessor;

    @Autowired
    private DepositService depositService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CalculationEngine calculationEngine;


    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        depositProcessor = new DepositProcessorImpl(depositService, accountService, notificationService, calculationEngine);
    }

    @Test
    public void testDepositProcessorConstructor(){

    }

    @Test
    public void testRetrieveDepositsFromDB(){

        DepositsEntity depositsEntity = createDepositsEntity(ScheduleType.DAILY, "Transfer 1", "A1", 1, 1, new BigDecimal("1215"), LocalDate.now(), LocalTime.now());
        List<DepositsEntity> depositsEntities = List.of(depositsEntity);
        Deposit deposit = convertDepositEntityToDeposit(depositsEntity);
        List<Deposit> depositList = List.of(deposit);

        when(depositService.findByUserID(1)).thenReturn(depositsEntities);
        List<Deposit> actualDeposits = depositProcessor.retrieveUserDeposits(1);
        assertNotNull(actualDeposits);
        assertEquals(depositList.size(), actualDeposits.size());
        assertEquals(depositList.get(0).getDepositID(), actualDeposits.get(0).getDepositID());
    }

    @Test
    public void testRetrieveUserDeposits_InvalidUserID(){
        DepositsEntity depositsEntity = createDepositsEntity(ScheduleType.DAILY, "Transfer 1", "A1", 1, 1, new BigDecimal("1215"), LocalDate.now(), LocalTime.now());
        List<DepositsEntity> depositsEntities = List.of(depositsEntity);
        Deposit deposit = convertDepositEntityToDeposit(depositsEntity);
        List<Deposit> depositList = List.of(deposit);

      //  when(depositService.findByUserID(-1)).thenThrow(RuntimeException.class);
        assertThrows(InvalidUserIDException.class, () -> {
            depositProcessor.retrieveUserDeposits(-1);
        });
    }

    @Test
    public void retrieveUserDeposits_whenDepositsEmpty_returnRuntimeException(){
        DepositsEntity depositsEntity = createDepositsEntity(ScheduleType.DAILY, "Transfer 1", "A1", 1, 1, new BigDecimal("1215"), LocalDate.now(), LocalTime.now());
        List<DepositsEntity> depositsEntities = List.of(depositsEntity);
        Deposit deposit = convertDepositEntityToDeposit(depositsEntity);
        List<Deposit> depositList = List.of(deposit);

        when(depositService.findByUserID(1)).thenReturn(new ArrayList<>());
        assertThrows(RuntimeException.class, () -> {
            depositProcessor.retrieveUserDeposits(1);
        });

    }

    @Test
    public void testGetAccountBalanceMapCalculation_EmptyList(){
        List<Deposit> depositListEmpty = new ArrayList<>();

        Map<Integer, BigDecimal> accountBalances = depositProcessor.getAccountBalanceMapCalculation(depositListEmpty);

        assertEquals(depositListEmpty.size(), accountBalances.size());
    }

    @Test
    public void testRetrieveCurrentAccountBalances_ValidAcctIDs(){
        Set<Integer> acctIDSet = new HashSet<>();
        acctIDSet.add(1);
        acctIDSet.add(2);
        acctIDSet.add(3);

        Map<Integer, BigDecimal> expectedCurrentAccountBalances = new HashMap<>();
        expectedCurrentAccountBalances.put(1, new BigDecimal("4500.000"));
        expectedCurrentAccountBalances.put(2, new BigDecimal("8700.000"));
        expectedCurrentAccountBalances.put(3, new BigDecimal("1414.000"));

        Map<Integer, BigDecimal> actualCurrentBalances = depositProcessor.retrieveCurrentAccountBalancesByAcctID(acctIDSet);

        assertEquals(expectedCurrentAccountBalances.size(), actualCurrentBalances.size());
        for(int i = 0; i < acctIDSet.size(); i++){
            assertEquals(expectedCurrentAccountBalances.get(i), actualCurrentBalances.get(i));
        }
    }

    @Test
    public void testRetrieveCurrentAccountBalances_InvalidAcctIDs(){
        Set<Integer> acctIDSet = new HashSet<>();
        acctIDSet.add(-1);
        acctIDSet.add(0);
        acctIDSet.add(-2);

        assertThrows(IllegalArgumentException.class, () -> {
            depositProcessor.retrieveCurrentAccountBalancesByAcctID(acctIDSet);
        });
    }

    @Test
    public void testRetrieveCurrentAccountBalances_EmptySetAcctIDs(){
        Set<Integer> emptyAccountIDSet = new HashSet<>();


        assertThrows(IllegalArgumentException.class, () -> {
            Map<Integer, BigDecimal> accountBalancesMap = depositProcessor.retrieveCurrentAccountBalancesByAcctID(emptyAccountIDSet);
        });
    }

    private DepositsEntity createDepositsEntity(ScheduleType scheduleType, String description, String acctCode, int acctID, int userID,
                                                BigDecimal amount, LocalDate scheduledDate, LocalTime timeScheduled){
        DepositsEntity depositsEntity = new DepositsEntity();
        depositsEntity.setAccount(AccountEntity.builder().accountCode(acctCode).acctID(acctID).build());
        depositsEntity.setDepositID(1);
        depositsEntity.setUser(UserEntity.builder().userID(userID).build());
        depositsEntity.setPosted(LocalDate.now());
        depositsEntity.setDescription(description);
        depositsEntity.setAmount(amount);
        depositsEntity.setScheduledTime(timeScheduled);
        depositsEntity.setScheduleInterval(scheduleType);
        depositsEntity.setScheduledDate(scheduledDate);
        return depositsEntity;
    }

    private Deposit convertDepositEntityToDeposit(final DepositsEntity depositsEntity){
        Deposit deposit = new Deposit();
        deposit.setDepositID(depositsEntity.getDepositID());
        deposit.setScheduleInterval(depositsEntity.getScheduleInterval());
        deposit.setDateScheduled(depositsEntity.getScheduledDate());
        deposit.setDescription(depositsEntity.getDescription());
        deposit.setAcctCode(depositsEntity.getAccount().getAccountCode());
        deposit.setUserID(depositsEntity.getUser().getUserID());
        deposit.setAmount(depositsEntity.getAmount());
        deposit.setTimeScheduled(depositsEntity.getScheduledTime());
        deposit.setAccountID(depositsEntity.getAccount().getAcctID());
        deposit.setDate_posted(LocalDate.now());
        return deposit;
    }

    @AfterEach
    void tearDown() {
    }
}