package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.converter.BillPaymentConverter;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.workbench.scheduler.BillPaymentScheduler;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BillPaymentSchedulerRunnerTest {

    @InjectMocks
    private BillPaymentSchedulerRunner billPaymentSchedulerRunner;

    @Mock
    private BillPaymentScheduler billPaymentScheduler;

    @Mock
    private BillPaymentConverter billPaymentConverter;

    @BeforeEach
    void setUp() {
        billPaymentSchedulerRunner = new BillPaymentSchedulerRunner(billPaymentScheduler, billPaymentConverter);
    }

    @Test
    @DisplayName("Test GetBillPaymentEntityConvertedToBillPayment model when bill payment entities is null, then throw exception")
    public void testGetBillPaymentEntityConvertedToBillPaymentModelWhenBillPaymentEntitiesIsNull() {
        Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities = new ArrayList<>();
        assertThrows(IllegalStateException.class, () -> {
            billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(null, billPaymentScheduleEntities);
        });
    }

    @Test
    @DisplayName("Test GetBillPaymentEntityConvertedToBillPayment when bill payment schedules null, then throw exception")
    public void testGetBillPaymentEntityConvertedToBillPaymentSchedulesWhenBillPaymentSchedulesIsNull() {
        Collection<BillPaymentEntity> billPaymentEntities = new ArrayList<>();
        assertThrows(IllegalStateException.class, () -> {
            billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, null);
        });
    }

    @Test
    @DisplayName("Test GetBillPaymentEntityConvertedToBillPayment when bill payment and bill payment schedules are null, then throw exception")
    public void testGetBillPaymentEntityConvertedToBillPaymentSchedulesWhenBillPaymentSchedulesAreNull() {
        assertThrows(IllegalStateException.class, () -> {
            billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(null, null);
        });
    }

    @Test
    @DisplayName("Test GetBillPaymentEntityConvertedToBillPayment when bill payment list has null payment then throw exception")
    public void testGetBillPaymentEntityConvertedToBillPaymentSchedulesWhenBillPaymentListHasNull() {
        BillPaymentScheduleEntity billPaymentScheduleEntity = BillPaymentScheduleEntity.builder()
                .paymentScheduleID(1L)
                .scheduledPaymentDate(LocalDate.of(2024, 5, 28))
                .paymentDueDate(LocalDate.of(2024, 6, 1))
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .autoPayEnabled(true)
                .isRecurring(true)
                .build();

        Collection<BillPaymentEntity> billPaymentScheduleEntities = Arrays.asList(null);
        Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities1 = Arrays.asList(billPaymentScheduleEntity);

        assertThrows(IllegalStateException.class, () -> {
            billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(billPaymentScheduleEntities, billPaymentScheduleEntities1);
        });
    }

    @Test
    @DisplayName("Test GetBillPaymentEntityConvertedToBillPayment when BillPaymentEntity parameters are null, then throw exception")
    public void testGetBillPaymentEntityConvertedToBillPaymentEntityWhenBillPaymentEntityParametersAreNull() {
        BillPaymentEntity billPaymentEntity = BillPaymentEntity.builder()
                .paymentAmount(new BigDecimal("100.00"))
                .paymentType("ACCOUNT")
                .paymentID(1L)
                .user(UserEntity.builder().userID(1).build())
                .payeeName("Payee Test")
                .account(AccountEntity.builder().acctID(1).build())
                .postedDate(LocalDate.now())
                .build();

        BillPaymentScheduleEntity billPaymentScheduleEntity = BillPaymentScheduleEntity.builder()
                .paymentScheduleID(1L)
                .scheduledPaymentDate(LocalDate.of(2024, 5, 28))
                .paymentDueDate(LocalDate.of(2024, 6, 1))
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .autoPayEnabled(true)
                .isRecurring(true)
                .build();

        List<BillPaymentEntity> billPaymentEntities = Arrays.asList(billPaymentEntity);
        List<BillPaymentScheduleEntity> billPaymentScheduleEntities = Arrays.asList(billPaymentScheduleEntity, billPaymentScheduleEntity);

        List<BillPayment> actual = billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);

        assertEquals(2, actual.size());
    }

    static Stream<Arguments> billPaymentEntitiesProvider()
    {
        return Stream.of(
                // Arguments are: test name, list of BillPaymentEntity, list of BillPaymentScheduleEntity, and expected exception class
                Arguments.of(
                        "Test when bill payment entities is null then throw exception",
                        Collections.emptyList(),
                        Collections.singletonList(createBillPaymentScheduleEntity()),
                        IllegalArgumentException.class
                ),
                Arguments.of(
                        "Test when bill payment schedules is null then throw exception",
                        Collections.singletonList(createBillPaymentEntity()),
                        null,
                        IllegalStateException.class
                ),
                Arguments.of(
                        "Test when bill payment entities and bill payment schedules are null then throw exception",
                        null,
                        null,
                        IllegalStateException.class
                ),
                Arguments.of(
                        "Test when bill payment list has null payment then throw exception",
                        new ArrayList<BillPaymentEntity>(){{add(null);}},
                        Collections.singletonList(createBillPaymentScheduleEntity()),
                        IllegalStateException.class
                )
        );
    }

    static BillPaymentEntity createBillPaymentEntity() {
        // replace with actual BillPaymentEntity creation
        return BillPaymentEntity.builder()
                .paymentAmount(new BigDecimal("100.00"))
                .paymentType("ACCOUNT")
                .paymentID(1L)
                .user(UserEntity.builder().userID(1).build())
                .payeeName("Payee Test")
                .account(AccountEntity.builder().acctID(1).build())
                .postedDate(LocalDate.now())
                .isProcessed(false)
                .paymentSchedule(null) // You may need to adjust this according to your test cases
                .build();
    }

    static BillPaymentScheduleEntity createBillPaymentScheduleEntity() {
        // replace with actual BillPaymentScheduleEntity creation
        return BillPaymentScheduleEntity.builder()
                .paymentScheduleID(1L)
                .scheduledPaymentDate(LocalDate.of(2024, 5, 28))
                .paymentDueDate(LocalDate.of(2024, 6, 1))
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .isRecurring(true)
                .autoPayEnabled(true)
                .billPayments(new HashSet<>()) // You may need to adjust this according to your test cases
                .billPaymentHistory(null)  // You may need to adjust this according to your test cases
                .build();
    }

    @ParameterizedTest
    @MethodSource("billPaymentEntitiesProvider")
    void testGetBillPaymentEntityConvertedToBillPaymentModel(String testName, List<BillPaymentEntity> billPaymentEntities, List<BillPaymentScheduleEntity> billPaymentScheduleEntities, Class<? extends Exception> expectedException) {
        if (expectedException != null) {
            Exception exception = assertThrows(expectedException, () -> billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities));
            assertTrue(true);
        } else {
            List<BillPayment> actual = billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);
            // Add assertion for expected result here when there's no expectation of exception
        }
    }

    @Test
    @DisplayName("Test GetBillPaymentEntityConvertedToBillPayment skips null entities and return valid list")
    public void testGetBillPaymentEntityConvertedToBillPaymentModelSkipsNullEntities() {
        // Arrange
        BillPaymentEntity billPaymentEntity1 = createBillPaymentEntity();
        BillPaymentScheduleEntity billPaymentScheduleEntity1 = createBillPaymentScheduleEntity();

        List<BillPaymentEntity> billPaymentEntities = Arrays.asList(billPaymentEntity1, null);
        List<BillPaymentScheduleEntity> billPaymentScheduleEntities = Arrays.asList(billPaymentScheduleEntity1, null);

        // Act
        List<BillPayment> actual = billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);

        // Assert
        assertEquals(1, actual.size()); // Assuming createBillPaymentEntity and createBillPaymentScheduleEntity are returning non-null values.
    }

    @Test
    @DisplayName("Test when both bill payment and schedule lists are empty")
    public void testGetBillPaymentEntityConvertedToBillPaymentModelWithEmptyLists() {
        //Arrange
        List<BillPaymentEntity> billPaymentEntities = Collections.emptyList();
        List<BillPaymentScheduleEntity> billPaymentScheduleEntities = Collections.emptyList();
        //Act
        List<BillPayment> actual = billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);
        //Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Test when the entities lists have non-null and null elements")
    public void testGetBillPaymentEntityConvertedToBillPaymentModelWithSomeNullEntities() {
        // Arrange
        BillPaymentEntity billPaymentEntity1 = createBillPaymentEntity();
        BillPaymentScheduleEntity billPaymentScheduleEntity1 = createBillPaymentScheduleEntity();

        List<BillPaymentEntity> billPaymentEntities = Arrays.asList(billPaymentEntity1, null, createBillPaymentEntity());
        List<BillPaymentScheduleEntity> billPaymentScheduleEntities = Arrays.asList(billPaymentScheduleEntity1, null, createBillPaymentScheduleEntity());

        // Act
        List<BillPayment> actual = billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);

        // Assert
        assertEquals(4, actual.size()); // Assuming createBillPaymentEntity and createBillPaymentScheduleEntity are returning non-null values.
    }

    @Test
    @DisplayName("Test when all entities in both lists are null")
    public void testGetBillPaymentEntityConvertedToBillPaymentModelWithAllNullEntities() {
        // Arrange
        List<BillPaymentEntity> billPaymentEntities = Arrays.asList(null, null);
        List<BillPaymentScheduleEntity> billPaymentScheduleEntities = Arrays.asList(null, null);

        // Act
        List<BillPayment> actual = billPaymentSchedulerRunner.getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Test fetch BillPaymentsFromDatabase")
    public void testFetchBillPaymentsFromDatabase() {
        Collection<BillPaymentEntity> billPaymentEntities = billPaymentSchedulerRunner.fetchBillPaymentsFromDatabase();

        assertEquals(2, billPaymentEntities.size());
    }

    @Test
    @DisplayName("Test fetch BillPaymentsFromDatabase when bill payments list is empty")
    public void testFetchBillPaymentsFromDatabaseWhenBillPaymentsListIsEmpty() {

    }

    @Test
    @DisplayName("Test run() when data fetch from database is non-empty")
    public void testRunWhenDataIsNotEmpty() {
        // Prepare some mock data
        List<BillPaymentEntity> billPaymentEntities = Arrays.asList(createBillPaymentEntity());
        List<BillPaymentScheduleEntity> billPaymentScheduleEntities = Arrays.asList(createBillPaymentScheduleEntity());

        // Prepare the mock environment
        when(billPaymentSchedulerRunner.fetchBillPaymentsFromDatabase()).thenReturn(billPaymentEntities);
        when(billPaymentSchedulerRunner.fetchBillPaymentSchedulesFromDatabase()).thenReturn(billPaymentScheduleEntities);

        // Now run the tested method
        billPaymentSchedulerRunner.run();

//        // Verify that getBillPaymentEntityConvertedToBillPaymentModel has been called with the right parameters
//        verify(billPaymentSchedulerRunner, times(1)).getBillPaymentEntityConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);
//
//        // Verify that scheduleBatchPayments has been called with any list of BillPayment object
//        verify(billPaymentSchedulerRunner, times(1)).scheduleBatchPayments(any(List.class));
    }




    @AfterEach
    void tearDown() {
    }
}