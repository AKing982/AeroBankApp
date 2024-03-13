package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidBalanceException;
import com.example.aerobankapp.exceptions.InvalidDepositException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.model.DepositBalanceSummary;
import com.example.aerobankapp.model.TransactionDetail;
import com.example.aerobankapp.repositories.AccountSecurityRepository;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.AccountIDResponse;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

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
    private AccountSecurityService accountSecurityService;

    @Autowired
    private AccountSecurityRepository accountSecurityRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private CalculationEngine calculationEngine;

    @Autowired
    private EncryptionService encryptionService;


    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        accountSecurityService = new AccountSecurityServiceImpl(accountSecurityRepository);
        depositProcessor = new DepositProcessorImpl(depositService, accountService, accountSecurityService, notificationService, calculationEngine, userLogService, encryptionService);
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

        Map<Integer, BigDecimal> accountBalances = depositProcessor.getCalculatedAccountBalanceMap(depositListEmpty);

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

    @Test
    public void testGetMinimumBalanceRequirementsByAcctID_ValidAcctID(){
        final int acctID = 1;

        BigDecimal expectedMinimumBalance = new BigDecimal("120.000");
        BigDecimal foundMinimumBalance = depositProcessor.getMinimumBalanceRequirementsByAcctID(acctID);

        assertNotNull(foundMinimumBalance);
        assertEquals(expectedMinimumBalance, foundMinimumBalance);
    }

    private static Stream<Arguments> provideAcctIDsForTesting() {
        return Stream.of(
                Arguments.of(1, new BigDecimal("120.000")),
                Arguments.of(2, new BigDecimal("250.000")),
                Arguments.of(3, new BigDecimal("1200.000")) // Assuming a scenario where null is returned to trigger the RuntimeException
        );
    }

    @ParameterizedTest
    @MethodSource("provideAcctIDsForTesting")
    void testGetMinimumBalanceRequirementsByAcctID(int acctID, BigDecimal expectedMinimumBalance) {
        if (expectedMinimumBalance != null) {
           // when(accountSecurityService.getMinimumBalanceRequirementsByAcctID(acctID)).thenReturn(expectedMinimumBalance);
            BigDecimal result = accountSecurityService.getMinimumBalanceRequirementsByAcctID(acctID);
            assertEquals(expectedMinimumBalance, result);
        } else if(expectedMinimumBalance == null) {
          //  when(accountSecurityService.getMinimumBalanceRequirementsByAcctID(acctID)).thenReturn(null);
            assertThrows(RuntimeException.class, () -> accountSecurityService.getMinimumBalanceRequirementsByAcctID(acctID));
        }
    }

    @Test
    public void testGetCalculationAccountBalanceMap(){
        Deposit deposit = createDeposit(1, 1, "A1", new BigDecimal("1215"), "Transfer 1", ScheduleType.DAILY, LocalDate.now(), LocalTime.now());
        Deposit deposit1 = createDeposit(1, 2, "A2", new BigDecimal("4500"), "Savings Money", ScheduleType.ONCE, LocalDate.now(), LocalTime.now());

        List<Deposit> depositList = List.of(deposit1, deposit);
        Map<Integer, BigDecimal> expectedCalculatedBalances = new HashMap<>();
        expectedCalculatedBalances.put(1, new BigDecimal(("9000.000")));
        expectedCalculatedBalances.put(2, new BigDecimal(12300));

        Map<Integer, BigDecimal> actualCalculatedBalances = depositProcessor.getCalculatedAccountBalanceMap(depositList);

        assertEquals(expectedCalculatedBalances.size(), actualCalculatedBalances.size());
        for(int i = 0; i < expectedCalculatedBalances.size(); i++){
            assertEquals(expectedCalculatedBalances.get(i), actualCalculatedBalances.get(i));
        }
    }

    @Test
    public void testUpdatingAccountBalances(){
        Map<Integer, BigDecimal> accountBalancesMap = new HashMap<>();
        accountBalancesMap.put(1, new BigDecimal("4500.000"));
        accountBalancesMap.put(2, new BigDecimal("1500.000"));
        accountBalancesMap.put(3, new BigDecimal("12500.000"));

        int expectedUpdateResult = 1;
        int actualUpdateResult = depositProcessor.bulkUpdateAccountBalances(accountBalancesMap);

        assertEquals(expectedUpdateResult, actualUpdateResult);
    }

    @Test
    public void testGenerateDepositBalanceSummaryMap_EmptyList_EmptyMap(){
        List<Deposit> emptyDeposits = new ArrayList<>();
        Map<Integer, BigDecimal> emptyAccountBalances = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            depositProcessor.generateDepositBalanceSummaryMap(emptyDeposits, emptyAccountBalances);
        });
    }

    @Test
    public void testGenerateDepositBalanceSummaryMap() {
        // Setup test data
        Deposit deposit1 = createDeposit(1, 1, "A1", new BigDecimal("1215"), "Transfer 1", ScheduleType.ONCE, LocalDate.now(), LocalTime.now());
        Deposit deposit2 = createDeposit(2, 4, "B2", new BigDecimal("3500"), "Added to savings", ScheduleType.ONCE, LocalDate.now(), LocalTime.now());
        List<Deposit> depositList = List.of(deposit1, deposit2);

        Map<Integer, BigDecimal> accountBalances = new HashMap<>();
        accountBalances.put(1, new BigDecimal("2600"));
        accountBalances.put(4, new BigDecimal("6700"));

        // Expected outcomes
        Map<Integer, List<DepositBalanceSummary>> expectedBalanceSummaryMap = new HashMap<>();
        expectedBalanceSummaryMap.put(1, List.of(buildDepositBalanceSummary(deposit1, new BigDecimal("2600"))));
        expectedBalanceSummaryMap.put(4, List.of(buildDepositBalanceSummary(deposit2, new BigDecimal("6700"))));

        // Execute the method under test
        Map<Integer, List<DepositBalanceSummary>> actualBalanceSummariesMap = depositProcessor.generateDepositBalanceSummaryMap(depositList, accountBalances);

        // Assertions
        assertNotNull(actualBalanceSummariesMap);
        assertEquals(expectedBalanceSummaryMap.size(), actualBalanceSummariesMap.size());
        assertTrue(actualBalanceSummariesMap.keySet().containsAll(expectedBalanceSummaryMap.keySet()));
        for (Integer acctId : expectedBalanceSummaryMap.keySet()) {
            assertEquals(expectedBalanceSummaryMap.get(acctId), actualBalanceSummariesMap.get(acctId));
        }
    }

    @Test
    public void testGenerateDepositBalanceSummaryMapWithEmptyInputs() {
        // Test with empty deposit list and account balances
        assertThrows(IllegalArgumentException.class, () -> {
            depositProcessor.generateDepositBalanceSummaryMap(List.of(), new HashMap<>()).isEmpty();
        });
    }

    @Test
    public void testGenerateDepositBalanceSummaryMapWithUnmatchedAccounts() {
        // Setup test data where one deposit does not have a corresponding balance
        Deposit deposit1 = createDeposit(1, 1, "A1", new BigDecimal("100"), "Transfer", ScheduleType.ONCE, LocalDate.now(), LocalTime.now());
        List<Deposit> depositList = List.of(deposit1);

        Map<Integer, BigDecimal> accountBalances = new HashMap<>();
        // No balance for acctID 1
        accountBalances.put(2, new BigDecimal("500"));

        // Execute the method under test
        assertThrows(NullPointerException.class, () -> {
            depositProcessor.generateDepositBalanceSummaryMap(depositList, accountBalances);
        });
    }

    static Stream<Arguments> depositBalanceSummaryDataProvider() {
        return Stream.of(
                Arguments.of(
                        List.of(createDeposit(1, 1, "A1", new BigDecimal("1215"), "Transfer 1", ScheduleType.ONCE, LocalDate.now(), LocalTime.now())),
                        Map.of(1, new BigDecimal("2600")),
                        Map.of(1, List.of(new DepositBalanceSummary(/* parameters for your DepositBalanceSummary object */)))
                ),
                // Add more test cases here
                Arguments.of(
                        List.of(createDeposit(1, 2, "A2", new BigDecimal("60.00"), "Transfer to Savings", ScheduleType.ONCE, LocalDate.now(), LocalTime.now())), // Empty list of deposits
                        Map.of(2, new BigDecimal(1560)), // Empty map of account balances
                        Map.of(2, List.of(buildDepositBalanceSummary(createDeposit(1, 2, "A2", new BigDecimal("60.00"), "Transfer to Savings", ScheduleType.ONCE, LocalDate.now(), LocalTime.now()), new BigDecimal(1560)))) // Expected empty map of deposit balance summaries
                )
        );
    }

    @ParameterizedTest
    @MethodSource("depositBalanceSummaryDataProvider")
    void testGenerateDepositBalanceSummaryMap(List<Deposit> deposits, Map<Integer, BigDecimal> accountBalances, @NotNull Map<Integer, List<DepositBalanceSummary>> expected) {
        Map<Integer, List<DepositBalanceSummary>> actual = depositProcessor.generateDepositBalanceSummaryMap(deposits, accountBalances);

        assertEquals(expected.size(), actual.size(), "Map sizes differ");
        expected.forEach((acctId, summaries) -> {
            assertTrue(actual.containsKey(acctId), "Missing account ID: " + acctId);
            assertEquals(summaries, actual.get(acctId), "Mismatch in DepositBalanceSummary for account ID: " + acctId);
        });
    }

    @Test
    public void testConvertDepositSummaryToTransactionDetail_EmptyList(){
        List<DepositBalanceSummary> emptyBalanceSummary = new ArrayList<>();
       // List<TransactionDetail> transactionDetails = depositProcessor.convertDepositSummaryToTransactionDetail(emptyBalanceSummary);

        assertThrows(NonEmptyListRequiredException.class, () -> {
            depositProcessor.convertDepositSummaryToTransactionDetail(emptyBalanceSummary);
        });
    }

    @Test
    public void testConvertDepositSummaryToTransactionDetail(){
        Deposit deposit1 = createDeposit(1, 1, "A1", new BigDecimal("100"), "Transfer", ScheduleType.ONCE, LocalDate.now(), LocalTime.now());
        DepositBalanceSummary depositBalanceSummary = buildDepositBalanceSummary(deposit1, new BigDecimal("4600.000"));
        List<DepositBalanceSummary> depositBalanceSummaries = List.of(depositBalanceSummary);

        TransactionDetail transactionDetail = buildTransactionDetail(1, "A1", 1, new BigDecimal("1600"));
        List<TransactionDetail> transactionDetailList = List.of(transactionDetail);

        List<TransactionDetail> actualTransactionDetails = depositProcessor.convertDepositSummaryToTransactionDetail(depositBalanceSummaries);

        assertEquals(transactionDetailList.size(), actualTransactionDetails.size());
    }

    @Test
    public void testConvertDepositSummaryToTransactionDetail_NullDeposit(){
        DepositBalanceSummary depositBalanceSummary = buildDepositBalanceSummary(null, new BigDecimal("4600.00"));
        List<DepositBalanceSummary> depositBalanceSummaries = List.of(depositBalanceSummary);

        TransactionDetail transactionDetail = buildTransactionDetail(1, "A1", 1, new BigDecimal("1600"));
        List<TransactionDetail> transactionDetailList = List.of(transactionDetail);

        //List<TransactionDetail> actualTransactionDetails = depositProcessor.convertDepositSummaryToTransactionDetail(depositBalanceSummaries);

        assertThrows(InvalidDepositException.class, () -> {
            depositProcessor.convertDepositSummaryToTransactionDetail(depositBalanceSummaries);
        });
    }

    @Test
    public void testConvertDepositSummaryToTransctionDetail_NullBalance(){
        Deposit deposit1 = createDeposit(1, 1, "A1", new BigDecimal("100"), "Transfer", ScheduleType.ONCE, LocalDate.now(), LocalTime.now());
        DepositBalanceSummary depositBalanceSummary = buildDepositBalanceSummary(deposit1, null);
        List<DepositBalanceSummary> depositBalanceSummaries = List.of(depositBalanceSummary);

        TransactionDetail transactionDetail = buildTransactionDetail(1, "A1", 1, new BigDecimal("1600"));
        List<TransactionDetail> transactionDetailList = List.of(transactionDetail);

        //List<TransactionDetail> actualTransactionDetails = depositProcessor.convertDepositSummaryToTransactionDetail(depositBalanceSummaries);

        assertThrows(InvalidBalanceException.class, () -> {
            depositProcessor.convertDepositSummaryToTransactionDetail(depositBalanceSummaries);
        });
    }

    @Test
    public void testConvertDepositSummaryToTransactionDetail_NullBalance_NullDeposit(){
        DepositBalanceSummary depositBalanceSummary = buildDepositBalanceSummary(null, null);
        List<DepositBalanceSummary> depositBalanceSummaries = List.of(depositBalanceSummary);

        TransactionDetail transactionDetail = buildTransactionDetail(1, "A1", 1, new BigDecimal("1600"));
        List<TransactionDetail> transactionDetailList = List.of(transactionDetail);

        //List<TransactionDetail> actualTransactionDetails = depositProcessor.convertDepositSummaryToTransactionDetail(depositBalanceSummaries);

        assertThrows(InvalidDepositException.class, () -> {
            depositProcessor.convertDepositSummaryToTransactionDetail(depositBalanceSummaries);
        });
    }

    @Test
    public void testBulkUpdateAccountBalances_EmptyMap(){
        Map<Integer, BigDecimal> emptyMap = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            depositProcessor.bulkUpdateAccountBalances(emptyMap);
        });
    }

    @Test
    public void testSendDepositNotification(){

    }

    private TransactionDetail buildTransactionDetail(int userID, String acctCode, int acctID, BigDecimal balance){
        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setAccountCode(acctCode);
        transactionDetail.setBalance(balance);
        transactionDetail.setUserID(userID);
        transactionDetail.setAccountID(acctID);
        return transactionDetail;
    }

    private static DepositBalanceSummary buildDepositBalanceSummary(final Deposit deposit, final BigDecimal balanceAfterDeposit){
        DepositBalanceSummary depositBalanceSummary = new DepositBalanceSummary();
        depositBalanceSummary.setDeposit(deposit);
        depositBalanceSummary.setBalanceAfterDeposit(balanceAfterDeposit);
        depositBalanceSummary.setDateProcessed(LocalDate.now());
        return depositBalanceSummary;
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

    private static Deposit createDeposit(int userID, int acctID, String acctCode, BigDecimal amount, String description, ScheduleType interval, LocalDate date, LocalTime time){
        Deposit deposit = new Deposit();
        deposit.setDepositID(1);
        deposit.setAmount(amount);
        deposit.setDescription(description);
        deposit.setUserID(userID);
        deposit.setAccountID(acctID);
        deposit.setAcctCode(acctCode);
        deposit.setScheduleInterval(interval);
        deposit.setDateScheduled(date);
        deposit.setTimeScheduled(time);
        return deposit;
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