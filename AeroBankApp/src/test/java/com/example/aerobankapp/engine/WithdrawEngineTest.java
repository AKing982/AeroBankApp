package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import lombok.With;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class WithdrawEngineTest {

    @InjectMocks
    private WithdrawEngine withdrawEngine;

    @MockBean
    private WithdrawService withdrawService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

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




    @BeforeEach
    void setUp() {
        withdrawEngine = new WithdrawEngine(withdrawService, userService, accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
    }

    @Test
    public void testFetchAllMethod(){
        List<Withdraw> fetchedWithdrawals = withdrawEngine.fetchAll();

        assertNotNull(fetchedWithdrawals);
        assertEquals(1, fetchedWithdrawals.size());
    }

    private static Stream<List<Withdraw>> withdrawProvider(){
        return Stream.of(
                List.of(createWithdraw(1L, 1, 1, new BigDecimal("45.000"), "Withdraw Test", ScheduleType.ONCE, LocalDate.now(), LocalTime.now()),
                        createWithdraw(2L, 1, 2, new BigDecimal("120.000"), "Withdraw Test 2", ScheduleType.DAILY, LocalDate.now(), LocalTime.now()))
        );
    }

    @Test
    public void run_noTransactions_shouldNotProcess(){
        when(withdrawService.findAll()).thenReturn(Collections.emptyList());

        // Act
        withdrawEngine.run();

        // Assert
        verify(withdrawService, times(1)).findAll();
    }

    @Test
    void run_withTransactions_shouldProcessSuccessfully() {
        // Arrange
        List<WithdrawEntity> withdrawEntities = Arrays.asList(
                new WithdrawEntity(/* initialize with test data */),
                new WithdrawEntity(/* initialize with test data */)
        );
        when(withdrawService.findAll()).thenReturn(withdrawEntities);
        // Mock additional interactions as necessary

        // Act
        withdrawEngine.run();

        // Assert
        verify(withdrawService, times(1)).findAll();
        // Add assertions to verify interactions such as update balances, save balance histories, etc.
    }

    @Test
    void run_withException_shouldHandleErrorGracefully() {
        // Arrange
        when(withdrawService.findAll()).thenThrow(RuntimeException.class);

        // Act and Assert
        assertDoesNotThrow(() -> withdrawEngine.run());
        // Verify that the error was logged
    }


    @ParameterizedTest
    @MethodSource("withdrawProvider")
    public void testWithdrawRun(List<Withdraw> withdraws){
        List<WithdrawEntity> withdrawEntities = withdraws.stream()
                .map(this::convertWithdrawToEntity)
                .toList();

        when(withdrawService.findAll()).thenReturn(withdrawEntities);

        withdrawEngine.run();
    }

    private WithdrawEntity convertWithdrawToEntity(Withdraw withdraw){
        WithdrawEntity withdrawEntity = new WithdrawEntity();
        withdrawEntity.setWithdrawID(withdraw.getId());
        withdrawEntity.setAccount(AccountEntity.builder().acctID(withdraw.getFromAccountID()).build());
        withdrawEntity.setAmount(withdraw.getAmount());
        withdrawEntity.setUser(UserEntity.builder().userID(withdraw.getUserID()).build());
        withdrawEntity.setDescription(withdraw.getDescription());
        withdrawEntity.setScheduledDate(withdraw.getDateScheduled());
        withdrawEntity.setScheduledTime(withdraw.getTimeScheduled());
        withdrawEntity.setScheduledInterval(withdraw.getScheduleInterval());
        return withdrawEntity;
    }

    private static Withdraw createWithdraw(Long id, int userID, int fromAccountID, BigDecimal amount, String description, ScheduleType scheduleType, LocalDate dateScheduled, LocalTime timeScheduled){
        Withdraw withdraw = new Withdraw();
        withdraw.setId(id);
        withdraw.setScheduleInterval(scheduleType);
        withdraw.setAmount(amount);
        withdraw.setFromAccountID(fromAccountID);
        withdraw.setUserID(userID);
        withdraw.setDescription(description);
        withdraw.setPosted(LocalDate.now());
        withdraw.setCurrency(Currency.getInstance(Locale.US));
        withdraw.setTimeScheduled(timeScheduled);
        withdraw.setDateScheduled(dateScheduled);
        return withdraw;
    }

    @AfterEach
    void tearDown() {
    }
}