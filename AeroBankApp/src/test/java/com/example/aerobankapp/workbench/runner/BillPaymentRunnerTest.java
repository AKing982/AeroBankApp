package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.converter.BillPaymentConverter;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.workbench.data.BillPaymentDataManager;
import com.example.aerobankapp.workbench.processor.BillPaymentProcessor;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BillPaymentRunnerTest {

    @InjectMocks
    private BillPaymentRunner billPaymentRunner;

    @Mock
    private BillPaymentProcessor billPaymentProcessor;

    @Mock
    private BillPaymentConverter billPaymentConverter;

    @Mock
    private BillPaymentDataManager billPaymentDataManager;

    @BeforeEach
    void setUp() {
        billPaymentRunner = new BillPaymentRunner(billPaymentProcessor, billPaymentConverter, billPaymentDataManager);
    }

    @Test
    @DisplayName("Test GetAllBillPayments when result is null, return empty collection")
    public void testGetAllBillPayments_whenresultIsNull_returnEmptyCollection() {
        when(billPaymentRunner.getAllBillPayments()).thenReturn(null);

        Collection<BillPaymentEntity> billPaymentEntities = billPaymentRunner.getAllBillPayments();
        assertEquals(0, billPaymentEntities.size());
        assertTrue(billPaymentEntities == null || billPaymentEntities.isEmpty());
    }

    @Test
    @DisplayName("Test getAllBillPayments when result is empty, then return empty collection")
    public void testGetAllBillPayments_whenResultIsEmpty_returnEmptyCollection() {
        when(billPaymentRunner.getAllBillPayments()).thenReturn(new ArrayList<>());

        Collection<BillPaymentEntity> billPaymentEntities = billPaymentRunner.getAllBillPayments();
        assertEquals(0, billPaymentEntities.size());
        assertTrue(billPaymentEntities == null || billPaymentEntities.isEmpty());
    }

    @Test
    @DisplayName("Test getAllBillPayment Schedules when result is null, then return empty collection")
    public void testGetAllBillPaymentSchedules_whenresultIsNull_returnEmptyCollection() {
        when(billPaymentRunner.getAllBillPaymentSchedules()).thenReturn(null);

        Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities = billPaymentRunner.getAllBillPaymentSchedules();
        assertEquals(0, billPaymentScheduleEntities.size());
        //assertTrue(billPaymentScheduleEntities == null || billPaymentScheduleEntities.isEmpty());
    }

    @Test
    @DisplayName("Test GetBillPaymentEntitiesConvertedToBillPayment when billPayments null, and bill payment schedules valid, then return exception")
    public void testGetBillPaymentEntitiesConvertedToBillPayment_whenbillPaymentsIsNull_returnEmptyCollection() {
        BillPaymentEntity billPaymentEntity1 = createBillPaymentEntity();
        BillPaymentScheduleEntity billPaymentScheduleEntity1 = createBillPaymentScheduleEntity();

        Collection<BillPaymentEntity> billPaymentEntities = Arrays.asList(billPaymentEntity1, null, createBillPaymentEntity());
        Collection<BillPaymentScheduleEntity> billPaymentScheduleEntities = Arrays.asList(billPaymentScheduleEntity1, null, createBillPaymentScheduleEntity());

        // Act
        Collection<BillPayment> actual = billPaymentRunner.getBillPaymentEntitiesConvertedToBillPaymentModel(billPaymentEntities, billPaymentScheduleEntities);

        // Assert
        assertEquals(4, actual.size()); //
    }

    @Test
    @DisplayName("Test ScheduleAndExecuteAllPayments when treeMap is null, then return false")
    public void testScheduleAndExecuteAllPayments_whenTreeMapIsNull_returnFalse() {
        TreeMap<LocalDate, Collection<BillPayment>> billPaymentsByDate = new TreeMap<>();

        boolean result = billPaymentRunner.scheduleAndExecuteAllPayments(null);
        assertFalse(result);
    }

    @Test
    @DisplayName("Test ScheduleAndExecuteAllPayments when treeMap is empty, then return false")
    public void testScheduleAndExecuteAllPayments_whenTreeMapIsEmpty_returnFalse() {
        TreeMap<LocalDate, Collection<BillPayment>> billPaymentsByDate = new TreeMap<>();
        boolean result = billPaymentRunner.scheduleAndExecuteAllPayments(billPaymentsByDate);
        assertFalse(result);
    }

    @Test
    @DisplayName("Test ScheduleAndExecuteAllPayments when billPaymentsByDate valid, then return true")
    public void testScheduleAndExecuteAllPayments_whenBillPaymentsByDateValid_returnTrue() {

    }

    @Test
    @DisplayName("Test GroupBillPaymentsByPaymentDate when bill payment collection null, then return empty collection")
    public void testGroupBillPaymentsByPaymentDate_whenbillPaymentCollectionIsNull_returnEmptyCollection() {
        //when(billPaymentRunner.groupBillPaymentsByPaymentDate(null)).thenReturn(new TreeMap<>());

        TreeMap<LocalDate, List<BillPayment>> billPaymentsByDate = billPaymentRunner.groupBillPaymentsByPaymentDate(null);
        assertEquals(0, billPaymentsByDate.size());
    }

    @Test
    @DisplayName("Test GroupBillPaymentsByPaymentDate when bill payment collection is empty, then return empty treeMap")
    public void testGroupBillPaymentsByPaymentDate_whenbillPaymentCollectionIsEmpty_returnEmptyTreeMap() {
        TreeMap<LocalDate, List<BillPayment>> billPaymentsByDate = billPaymentRunner.groupBillPaymentsByPaymentDate(Collections.emptyList());
        assertEquals(0, billPaymentsByDate.size());
    }

    @Test
    @DisplayName("Test GroupBillPaymentsByPaymentDate when bill payments valid, then return TreeMap")
    public void testGroupBillPaymentsByPaymentDate_whenbillPaymentsValid_returnTreeMap() {

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

    @AfterEach
    void tearDown() {
    }
}