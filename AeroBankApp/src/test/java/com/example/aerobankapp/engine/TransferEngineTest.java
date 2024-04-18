package com.example.aerobankapp.engine;

import com.example.aerobankapp.converter.EntityToModelConverter;
import com.example.aerobankapp.converter.TransferConverter;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.NullTransferObjectException;
import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.TransferBalanceSummary;
import com.example.aerobankapp.model.TransferBalances;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.utilities.BalanceHistoryUtil;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
import com.example.aerobankapp.workbench.utilities.TransferType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class TransferEngineTest {

    @InjectMocks
    private TransferEngine transferEngine;

    @MockBean
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountSecurityService accountSecurityService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CalculationEngine calculationEngine;

    @Autowired
    private BalanceHistoryService balanceHistoryService;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountNotificationService accountNotificationService;

    private EntityToModelConverter<TransferEntity, Transfer> transferConverter = new TransferConverter();

    private static TransferEntity transferEntity;
    private static TransferEntity userTransfer;

    private static Transfer transfer1;

    private static Transfer transfer2;

    private static Transfer transfer3;

    private static Transfer transfer4;

    private static Transfer transfer5;

    private static Transfer Transfer6;


    @BeforeEach
    void setUp() {
        transferEntity =  createMockTransfer(1L,
                TransferType.SAME_USER, new BigDecimal("45.00"),
                "Checking to Savings transfer",
                1, 1, 1, 2);

        userTransfer = createMockTransfer(2L, TransferType.USER_TO_USER, new BigDecimal("250.00"), "Transfer to BSmith",
                1, 2, 1, 4);

        transfer1 = new Transfer(1, "Transfer test", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 1L, 1, 2, 1, 1, TransferType.SAME_USER);
        transfer2 = new Transfer(1, "Transfer Test 2", null, LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 2L, 1, 1, 1, 1, TransferType.USER_TO_USER);
        transfer3 = new Transfer(1, "Transfer test 3", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 3L , 1, 3, 1, 1, TransferType.USER_TO_USER);


        transferEngine = new TransferEngine(transferService, accountService, accountNotificationService, userService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
    }

    @Test
    public void testFindAllReturnsEmptyList(){
        when(transferService.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NonEmptyListRequiredException.class, () -> {
            transferEngine.fetchAll();
        });
    }

    @Test
    public void testFindAllReturnsNonEmptyList(){

        when(transferService.findAll()).thenReturn(List.of(transferEntity));

        List<Transfer> transfers = transferEngine.fetchAll();

        assertNotNull(transfers);
        assertFalse(transfers.isEmpty());
        assertEquals(1, transfers.size());
    }

    @Test
    public void testGetCalculatedAccountBalanceMap(){

    }


    @Test
    public void testRetrieveAccountIDsWithDistinctIDs(){
        List<TransferEntity> transferEntities = List.of(transferEntity, userTransfer);
        List<Transfer> transfers = transferEntities.stream().map(transferConverter::convert)
                .toList();

        Set<Integer> expectedFromAccountIDs = new HashSet<>(Arrays.asList(4, 2));
        Set<Integer> actualFromAccountIDs = transferEngine.retrieveTransferAccountIDSet(transfers, Transfer::getFromAccountID);
        assertEquals(expectedFromAccountIDs, actualFromAccountIDs);

        Set<Integer> expectedToAccountIDs = new HashSet<>(Arrays.asList(1));
        Set<Integer> actualToAccountIDs = transferEngine.retrieveTransferAccountIDSet(transfers, Transfer::getToAccountID);
        assertEquals(expectedToAccountIDs, actualToAccountIDs);
    }

    @Test
    public void testRetrieveAccountIDSetWithDuplicateIDs(){
        List<TransferEntity> transferEntities = List.of(transferEntity, userTransfer);
        List<Transfer> transfers = transferEntities.stream().map(transferConverter::convert)
                .toList();
    }

    @Test
    public void testRetrieveAccountIDsWithEmptyList(){
        List<Transfer> transfers = new ArrayList<>();
        assertThrows(NonEmptyListRequiredException.class, () -> {
            transferEngine.retrieveTransferAccountIDSet(transfers, Transfer::getFromAccountID);
        });
    }

    @Test
    public void testRetrieveTransactionAmountByAcctID_EmptyTransferList(){

        List<Transfer> emptyTransfers = new ArrayList<>();
        assertThrows(NonEmptyListRequiredException.class, () -> {
            transferEngine.retrieveTransactionAmountByAcctID(emptyTransfers);
        });
    }

    @Test
    public void testRetrieveTransactionAmountByAcctID_TransferListContainsNull(){

        assertThrows(NullPointerException.class, () -> {
            transferEngine.retrieveTransactionAmountByAcctID(Arrays.asList(null));
        });
    }

    private static Stream<Arguments> provideTransfersForTesting() {
        return Stream.of(
                Arguments.of( List.of(createMockTransferModel(1, "Transfer test", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(),  1L, 1, 2, 1, 1, TransferType.SAME_USER),
                                     createMockTransferModel(2, "Transfer test 2", new BigDecimal("200"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), 2L, 1, 3, 1, 1, TransferType.SAME_USER)
                        ),
                        new HashMap<Integer, BigDecimal>() {{
                            put(1, new BigDecimal("200"));
                        }}
                )

                // Assuming a constructor Transfer(fromAccountID, amount)
                // Arguments are: List of Transfers, Expected Output

                // Add more test cases as needed
        );
    }

    @ParameterizedTest
    @MethodSource("provideTransfersForTesting")
    void testRetrieveTransactionAmountByAcctID(List<Transfer> transfers, Map<Integer, BigDecimal> expected) {// Replace YourClassName with your actual class name
        Map<Integer, BigDecimal> actual = transferEngine.retrieveTransactionAmountByAcctID(transfers);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetTransferCalculation_NullInput(){

    }

    @Test
    public void testRetrieveTransferUserIDSet_EmptyTransfers(){

        List<Transfer> emptyTransfers = new ArrayList<>();

        Set<Integer> expectedUserIDs = new HashSet<>();
        expectedUserIDs.add(1);

       // Set<Integer> actualFromUserIDs = transferEngine.retrieveTransferUserIDSet()
    }

    @Test
    void testGetTransferCalculation_InitialFail() {
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal fromBalance = BigDecimal.valueOf(500);
        BigDecimal toBalance = BigDecimal.valueOf(200);

        TransferBalances result = transferEngine.getTransferCalculation(amount, toBalance, fromBalance);

        // This test is designed to fail, as the actual method should adjust the balances, not return them as-is.
        assertEquals(amount, result.getFromAccountBalance());
        assertEquals(amount, result.getToAccountBalance());
    }

    @Test
    void testGetTransferCalculation_FromAccountBalanceDeducted() {
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal fromBalance = BigDecimal.valueOf(500);

        TransferBalances result = transferEngine.getTransferCalculation(amount, null, fromBalance);

        // Assuming the calculation engine simply subtracts the amount from the fromAccountBalance
        BigDecimal expectedFromBalance = fromBalance.subtract(amount);

        assertEquals(expectedFromBalance, result.getFromAccountBalance());
    }

    @Test
    void testGetTransferCalculation_ToAccountBalanceIncreased() {
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal toBalance = BigDecimal.valueOf(200);
        BigDecimal fromAccountBalance = BigDecimal.valueOf(1500);

        TransferBalances result = transferEngine.getTransferCalculation(amount, toBalance, fromAccountBalance);

        // Assuming the calculation engine simply adds the amount to the toAccountBalance
        BigDecimal expectedToBalance = toBalance.add(amount);
        BigDecimal expectedFromBalance = fromAccountBalance.subtract(amount);

        assertEquals(expectedFromBalance, result.getFromAccountBalance());
        assertEquals(expectedToBalance, result.getToAccountBalance());
    }

    @Test
    void testGetTransferCalculation_NullAmount() {
        BigDecimal fromBalance = BigDecimal.valueOf(500);
        BigDecimal toBalance = BigDecimal.valueOf(200);

        TransferBalances result = transferEngine.getTransferCalculation(null, toBalance, fromBalance);

        // When the amount is null, we expect the original balances to be unchanged.
        assertNull(result.getFromAccountBalance());
        assertNull(result.getToAccountBalance());
    }

    @Test
    public void testFilterTransfersByType_EmptyList(){
        List<Transfer> emptyList = new ArrayList<>();

        Map<TransferType, List<Transfer>> filteredTransfers = transferEngine.filterTransfersByType(emptyList, TransferType.SAME_USER);

        assertNotNull(filteredTransfers);
        assertEquals(1, filteredTransfers.size());
    }

    @Test
    public void testFilterTransfersByType_return_SameUserTransfers(){
        Transfer transfer1 = new Transfer(1, "Transfer test", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 1L, 1, 2, 1, 1, TransferType.SAME_USER);
        Transfer transfer2 = new Transfer(1, "Transfer Test 2", null, LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 2L, 1, 1, 1, 1, TransferType.USER_TO_USER);
        Transfer transfer3 = new Transfer(1, "Transfer test 3", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 3L , 1, 3, 1, 1, TransferType.USER_TO_USER);

        List<Transfer> transfers = Arrays.asList(transfer1, transfer2, transfer3);

        Map<TransferType, List<Transfer>> sameTransfers = transferEngine.filterTransfersByType(transfers, TransferType.USER_TO_USER);
        Map<TransferType, List<Transfer>> userTransfers = transferEngine.filterTransfersByType(transfers, TransferType.SAME_USER);

        List<Transfer> sameTransferList = sameTransfers.get(TransferType.USER_TO_USER);
        List<Transfer> userTransferList = userTransfers.get(TransferType.SAME_USER);

        assertNotNull(sameTransfers);
        assertEquals(2, sameTransferList.size());
        assertEquals(1, userTransferList.size());
        assertEquals(1, sameTransfers.size());
    }

    @Test
    public void testFilterByType(){
        Transfer transfer1 = new Transfer(1, "Transfer test", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 1L, 1, 2, 1, 1, TransferType.SAME_USER);
        Transfer transfer2 = new Transfer(1, "Transfer Test 2", null, LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 2L, 1, 1, 1, 1, TransferType.USER_TO_USER);
        Transfer transfer3 = new Transfer(1, "Transfer test 3", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 3L , 1, 3, 1, 1, TransferType.USER_TO_USER);

        List<Transfer> transfers = Arrays.asList(transfer1, transfer2, transfer3);

        List<Transfer> sameAsList = transferEngine.filterByType(transfers, TransferType.USER_TO_USER);

        assertNotNull(transfers);
        assertEquals(2, sameAsList.size());
    }

    @Test
    public void testRetrieveTransactionAmountByAcctID_NullAmount(){
        final BigDecimal amount = null;

        Transfer transfer1 = new Transfer(1, "Transfer test", new BigDecimal("45.00"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 1L, 1, 2, 1, 1, TransferType.SAME_USER);
        Transfer transfer2 = new Transfer(1, "Transfer Test 2", new BigDecimal("120.000"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 2L, 4, 1, 1, 1, TransferType.USER_TO_USER);
        Transfer transfer3 = new Transfer(1, "Transfer test 3", null, LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 3L , 1, 3, 1, 1, TransferType.USER_TO_USER);

        List<Transfer> transfers = Arrays.asList(transfer1, transfer2, transfer3);

        Map<Integer, BigDecimal> expected = new HashMap<>();
        expected.put(1, new BigDecimal("45.00"));
        expected.put(2, new BigDecimal("120.00"));

        Map<Integer, BigDecimal> actual = transferEngine.retrieveTransactionAmountByAcctID(transfers);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(2, actual.size());
    }

    @Test
    public void testConvertBalanceSummaryToBalanceHistoryEntityList(){
        Transfer transfer1 = new Transfer(1, "Transfer test", new BigDecimal("45.00"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 1L, 1, 2, 1, 1, TransferType.SAME_USER);

        TransferBalanceSummary transferBalanceSummary = new TransferBalanceSummary();
        transferBalanceSummary.setToAccountPostBalance(new BigDecimal("4500.00"));
        transferBalanceSummary.setPostBalance(new BigDecimal("1120.00"));
        transferBalanceSummary.setDateProcessed(LocalDate.now());
        transferBalanceSummary.setTransaction(transfer1);

        List<TransferBalanceSummary> transferBalanceSummaries = Arrays.asList(transferBalanceSummary);

        BalanceHistoryEntity balanceHistoryEntity = createBalanceHistoryEntity(transferBalanceSummaries.get(0), new BigDecimal("1215"), new BigDecimal("120"));
        List<BalanceHistoryEntity> balanceHistoryEntities = Arrays.asList(balanceHistoryEntity);

        List<BalanceHistoryEntity> actualBalanceHistories = transferEngine.convertBalanceSummaryToBalanceHistoryEntityList(transferBalanceSummaries);

        assertNotNull(actualBalanceHistories);
        assertFalse(actualBalanceHistories.isEmpty());
        assertEquals(1, actualBalanceHistories.size());
    }

    @Test
    public void testProcessUserToUserTransfers(){
        Transfer transfer1 = new Transfer(1, "Transfer test", new BigDecimal("45.00"), LocalTime.now(),  LocalDate.now(), 1L, 1, 2,TransferType.SAME_USER);
        Transfer transfer2 = new Transfer("Transfer Test 2", new BigDecimal("120.000"), LocalTime.now(), LocalDate.now() , 2L, 4, 1, 1, 1, "B1", "35-09-24", TransferType.USER_TO_USER);
        Transfer transfer3 = new Transfer("Transfer test 3", new BigDecimal("12.000"), LocalTime.now(), LocalDate.now(), 3L , 1, 3, 1, 1, "A1", "89-42-48", TransferType.USER_TO_USER);

        List<Transfer> transfers = Arrays.asList(transfer1, transfer2, transfer3);

        boolean result = transferEngine.processUserToUserTransfers(transfers);
        assertTrue(result);
    }

    @Test
    public void testProcessSameUserTransfers(){
        Transfer transfer1 = new Transfer(1, "Transfer test", new BigDecimal("45.00"), LocalTime.now(),  LocalDate.now(), 1L, 1, 2,TransferType.SAME_USER);
        Transfer transfer2 = new Transfer(1, "Transfer Test 2", new BigDecimal("120.000"), LocalTime.now(), LocalDate.now() , 2L, 1, 2, TransferType.SAME_USER);
        Transfer transfer3 = new Transfer(1, "Transfer test 3", new BigDecimal("12.000"), LocalTime.now(), LocalDate.now(), 3L , 1, 3, TransferType.SAME_USER);

        List<Transfer> transfers = Arrays.asList(transfer1, transfer2, transfer3);

        boolean result = transferEngine.processSameUserTransfers(transfers);

        assertTrue(result);
    }



    @Test
    public void testGetTransferCalculation_NullAmount_NullBalances(){

        TransferBalances expected = new TransferBalances(new BigDecimal("2010"), new BigDecimal("1120"));
        TransferBalances actual = transferEngine.getTransferCalculation(null, null, null);

        assertEquals(expected.getToAccountBalance(), actual.getToAccountBalance());
    }

    @Test
    public void testGetNewAccountBalancesAfterTransfer_EmptyTransferMap_EmptyBalances(){
        Map<Integer, BigDecimal> emptyTransferAmount = new HashMap<>();
        Map<Integer, BigDecimal> emptyBalances = new HashMap<>();

        Map<Integer, BigDecimal> expectedNewBalances = new HashMap<>();
        expectedNewBalances.put(1, new BigDecimal("1215"));
        expectedNewBalances.put(2, new BigDecimal("3500"));

       assertThrows(NonEmptyListRequiredException.class, () -> {
           transferEngine.getNewAccountBalancesAfterTransfer(emptyTransferAmount, emptyBalances);
       });
    }

    @Test
    public void testGetNewAccountBalancesAfterTransfer_ValidParameters(){
        Map<Integer, BigDecimal> expectedNewBalances = new HashMap<>();
        expectedNewBalances.put(1, new BigDecimal("1215"));
        expectedNewBalances.put(2, new BigDecimal("3500"));

        Map<Integer, BigDecimal> transferAmountMap = new HashMap<>();
        Map<Integer, BigDecimal> currentBalances = new HashMap<>();

        transferAmountMap.put(1,  new BigDecimal("45.00"));
        transferAmountMap.put(2, new BigDecimal("120.00"));
        currentBalances.put(1, new BigDecimal("1020"));
        currentBalances.put(2, new BigDecimal("3450"));

        Map<Integer, BigDecimal> actual = transferEngine.getNewAccountBalancesAfterTransfer(transferAmountMap, currentBalances);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(expectedNewBalances.size(), actual.size());
    }


    private static Transfer createMockTransferModel(int userID, String description,
                                                    BigDecimal amount,
                                                    LocalTime time,
                                                    ScheduleType interval,
                                                    LocalDate dateScheduled,
                                                    Long transferID,
                                                    int fromAccountID,
                                                    int toAccountID,
                                                    int originUserID,
                                                    int targetUserID,
                                                    TransferType transferType){
        Transfer transfer = new Transfer();
        transfer.setTransferID(transferID);
        transfer.setPending(false);
        transfer.setAmount(amount);
        transfer.setPosted(LocalDate.now());
        transfer.setDescription(description);
        transfer.setCurrency(Currency.getInstance(Locale.US));
        transfer.setFromAccountID(fromAccountID);
        transfer.setToAccountID(toAccountID);
        transfer.setFromUserID(originUserID);
        transfer.setToUserID(targetUserID);
        transfer.setDateScheduled(dateScheduled);
        transfer.setScheduleInterval(interval);
        transfer.setTimeScheduled(time);
        transfer.setTransferType(transferType);
        return transfer;
    }


    private static Transfer createTransfer(TransferEntity entity){
        Transfer transfer = new Transfer();
        transfer.setTransferID(entity.getTransferID());
        transfer.setTransferType(entity.getTransferType());
        transfer.setFromAccountID(entity.getFromAccount().getAcctID());
        transfer.setFromUserID(entity.getFromUser().getUserID());
        transfer.setToUserID(entity.getToUser().getUserID());
        transfer.setAmount(entity.getTransferAmount());
        transfer.setDescription(entity.getDescription());
        transfer.setPosted(LocalDate.now());
        transfer.setToAccountID(entity.getToAccount().getAcctID());
        transfer.setToAccountCode(entity.getToAccount().getAccountCode());
        transfer.setToAccountNumber(entity.getToUser().getAccountNumber());
        return transfer;
    }

    private TransferEntity convertTransferToEntity(Transfer transferEntity){
        TransferEntity entity = new TransferEntity();
        entity.setTransferID(transferEntity.getTransferID());
        entity.setTransferAmount(transferEntity.getAmount());
        entity.setTransferType(transferEntity.getTransferType());
        entity.setStatus(TransferStatus.PENDING);
        entity.setToUser(UserEntity.builder().userID(transferEntity.getToUserID()).build());
        entity.setFromUser(UserEntity.builder().userID(transferEntity.getFromUserID()).build());
        entity.setToAccount(AccountEntity.builder().acctID(transferEntity.getToAccountID()).build());
        entity.setFromAccount(AccountEntity.builder().acctID(transferEntity.getFromAccountID()).build());
        entity.setDescription(transferEntity.getDescription());
        entity.setTransferTime(transferEntity.getTimeScheduled());
        entity.setTransferDate(transferEntity.getDateScheduled());
        return entity;
    }


    private static TransferEntity createMockTransfer(Long id,
                                              TransferType transferType,
                                              BigDecimal amount,
                                              String description,
                                              int fromUserID,
                                              int toUserID,
                                              int toAccountID,
                                              int fromAccountID){
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setTransferID(id);
        transferEntity.setTransferType(transferType);
        transferEntity.setTransferAmount(amount);
        transferEntity.setDescription(description);
        transferEntity.setFromUser(UserEntity.builder().userID(fromUserID).build());
        transferEntity.setToUser(UserEntity.builder().userID(toUserID).build());
        transferEntity.setFromAccount(AccountEntity.builder().acctID(fromAccountID).build());
        transferEntity.setToAccount(AccountEntity.builder().acctID(toAccountID).build());;
        transferEntity.setTransferTime(LocalTime.now());
        transferEntity.setTransferDate(LocalDate.now());
        transferEntity.setStatus(TransferStatus.PENDING);
        return transferEntity;

    }

    private BalanceHistoryEntity createBalanceHistoryEntity(TransferBalanceSummary transferBalanceSummary, BigDecimal currentBalance, BigDecimal adjusted){
        if(transferBalanceSummary != null){
            if(currentBalance != null || adjusted != null){
                BalanceHistory balanceHistory = BalanceHistoryUtil.convertTransferBalanceSummaryToBalanceHistoryModel(transferBalanceSummary, currentBalance, adjusted);
                return BalanceHistoryUtil.convertBalanceHistoryToEntity(balanceHistory);
            }
        }
        return null;
    }


    @AfterEach
    void tearDown() {
    }
}