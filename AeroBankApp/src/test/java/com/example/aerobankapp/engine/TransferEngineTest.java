package com.example.aerobankapp.engine;

import com.example.aerobankapp.converter.EntityToModelConverter;
import com.example.aerobankapp.converter.TransferConverter;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.NullTransferObjectException;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
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
                false, new BigDecimal("45.00"),
                "Checking to Savings transfer",
                1, 1, 1, 2);

        userTransfer = createMockTransfer(2L, true, new BigDecimal("250.00"), "Transfer to BSmith",
                1, 2, 1, 4);

        transfer1 = new Transfer(1, "Transfer test", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 1L, 1, 2, 1, 1, false);
        transfer2 = new Transfer(1, "Transfer Test 2", null, LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 2L, 1, 1, 1, 1, false);
        transfer3 = new Transfer(1, "Transfer test 3", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), LocalDate.now(), Currency.getInstance(Locale.US), 3L , 1, 3, 1, 1, false);


        transferEngine = new TransferEngine(transferService, accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
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
                Arguments.of( List.of(createMockTransferModel(1, "Transfer test", new BigDecimal("45"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(),  1L, 1, 2, 1, 1, false),
                                     createMockTransferModel(2, "Transfer test 2", new BigDecimal("200"), LocalTime.now(), ScheduleType.ONCE, LocalDate.now(), 2L, 1, 3, 1, 1, false)
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
                                                    boolean isUserTransfer){
        Transfer transfer = new Transfer();
        transfer.setTransferID(transferID);
        transfer.setPending(false);
        transfer.setAmount(amount);
        transfer.setPosted(LocalDate.now());
        transfer.setDescription(description);
        transfer.setCurrency(Currency.getInstance(Locale.US));
        transfer.setFromAccountID(fromAccountID);
        transfer.setToAccountID(toAccountID);
        transfer.setOriginUserID(originUserID);
        transfer.setTargetUserID(targetUserID);
        transfer.setDateScheduled(dateScheduled);
        transfer.setScheduleInterval(interval);
        transfer.setTimeScheduled(time);
        transfer.setUserToUserTransfer(isUserTransfer);
        return transfer;
    }


    private static Transfer createTransfer(TransferEntity entity){
        Transfer transfer = new Transfer();
        transfer.setTransferID(entity.getTransferID());
        transfer.setUserToUserTransfer(entity.isUserTransfer());
        transfer.setFromAccountID(entity.getFromAccount().getAcctID());
        transfer.setOriginUserID(entity.getFromUser().getUserID());
        transfer.setTargetUserID(entity.getToUser().getUserID());
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
        entity.setDateTransferred(LocalDate.now());
        entity.setUserTransfer(transferEntity.isUserToUserTransfer());
        entity.setStatus(TransferStatus.PENDING);
        entity.setToUser(UserEntity.builder().userID(transferEntity.getTargetUserID()).build());
        entity.setFromUser(UserEntity.builder().userID(transferEntity.getOriginUserID()).build());
        entity.setToAccount(AccountEntity.builder().acctID(transferEntity.getToAccountID()).build());
        entity.setFromAccount(AccountEntity.builder().acctID(transferEntity.getFromAccountID()).build());
        entity.setDescription(transferEntity.getDescription());
        entity.setScheduledTime(transferEntity.getTimeScheduled());
        entity.setScheduledDate(transferEntity.getDateScheduled());
        entity.setPending(transferEntity.isPending());
        return entity;
    }


    private static TransferEntity createMockTransfer(Long id,
                                              boolean isUserTransfer,
                                              BigDecimal amount,
                                              String description,
                                              int fromUserID,
                                              int toUserID,
                                              int toAccountID,
                                              int fromAccountID){
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setTransferID(id);
        transferEntity.setUserTransfer(isUserTransfer);
        transferEntity.setTransferAmount(amount);
        transferEntity.setDescription(description);
        transferEntity.setFromUser(UserEntity.builder().userID(fromUserID).build());
        transferEntity.setToUser(UserEntity.builder().userID(toUserID).build());
        transferEntity.setFromAccount(AccountEntity.builder().acctID(fromAccountID).build());
        transferEntity.setToAccount(AccountEntity.builder().acctID(toAccountID).build());
        transferEntity.setDateTransferred(LocalDate.now());
        transferEntity.setScheduledTime(LocalTime.now());
        transferEntity.setScheduledDate(LocalDate.now());
        transferEntity.setStatus(TransferStatus.PENDING);
        return transferEntity;

    }


    @AfterEach
    void tearDown() {
    }
}