package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.exceptions.NullHistoryCriteriaException;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryBuilderImplTest {

    private QueryBuilder queryBuilder;

    private StringBuilder query;

    @Mock
    private HistoryCriteria historyCriteria;

    @BeforeEach
    void setUp() {
        queryBuilder = new QueryBuilderImpl();
        query = new StringBuilder();
        historyCriteria = mock(HistoryCriteria.class);
    }

    @Test
    public void testBuildQueryConditions_NullCriteria(){
        assertThrows(NullHistoryCriteriaException.class, () -> {
            queryBuilder.buildQueryConditions(null, query);
        });
    }

    @Test
    public void shouldAddDepositsEntityTableWithDepositsType(){
        TransactionType transactionType = TransactionType.DEPOSIT;

        StringBuilder expected = new StringBuilder("SELECT e FROM DepositsEntity e ");
        StringBuilder actual = queryBuilder.buildTableStatement(transactionType);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void shouldAddWithdrawEntityTableWithWithdrawType(){
        TransactionType transactionType = TransactionType.WITHDRAW;

        StringBuilder expected = new StringBuilder("SELECT e FROM WithdrawEntity e ");
        StringBuilder actual = queryBuilder.buildTableStatement(transactionType);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void shouldAddTransferEntityTableWithTransferType(){
        TransactionType transactionType = TransactionType.TRANSFER;

        StringBuilder expected = new StringBuilder("SELECT e FROM TransferEntity e ");
        StringBuilder actual = queryBuilder.buildTableStatement(transactionType);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testShouldReturnEmptyConditionsForEmptyCriteria(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, null, null, null, null, null, null, null, null, null);
        List<String> conditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        assertTrue(conditions.isEmpty());
        assertTrue(query.toString().isEmpty());
    }

    @Test
    public void shouldAddDescriptionCondition(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "test", null, null, null, null, null, null, null, null);
        List<String> conditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        assertTrue(conditions.contains("e.description LIKE :descr"),"Conditions should contain description condition");
        assertTrue(query.toString().contains("e.description LIKE :descr"));
    }

    @Test
    void shouldAppendWhereToQueryIfConditionsExist() {
        HistoryCriteria criteria = new HistoryCriteria(1, "test", null, null, null, null, null, null, null, null);
        queryBuilder.buildQueryConditions(criteria, query);
        assertTrue(query.toString().startsWith("WHERE"), "Query should start with WHERE when conditions exist");
    }


    @Test
    public void testBuildQueryConditions_DescriptionAndElseBlank(){
        StringBuilder query = new StringBuilder();
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "Test", null, null, null, null, null, null, null, null);

        // Expected condition strings
        List<String> expectedConditions = List.of("WHERE", "e.description LIKE :descr", "AND e.user.userID =:userID");
        // Build actual conditions
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);

        // Check conditions
        assertNotNull(actualConditions, "Condition list should not be null");
        assertEquals(expectedConditions.size(), actualConditions.size(), "Condition size should match expected");
        assertEquals(expectedConditions.get(0), actualConditions.get(0), "First condition should be WHERE");
        assertEquals(expectedConditions.get(1), actualConditions.get(1), "Second condition should be the description LIKE clause");

        // Check the actual query string built
        String expectedQueryString = "WHERE e.description LIKE :descr AND e.user.userID =:userID";
        assertEquals(expectedQueryString, query.toString(), "Query string should contain WHERE clause and description condition");
    }

    @Test
    public void shouldAddDescriptionAndStartDateConditions(){

        HistoryCriteria historyCriteria = new HistoryCriteria(1, "test", null, null, LocalDate.of(2024, 5, 5), null, null, null, null, null);

        List<String> expectedConditions = List.of("WHERE", "e.description LIKE :descr", "AND e.scheduledDate =:startDate", "AND e.user.userID=:userID");
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        System.out.println(actualConditions);

        assertNotNull(actualConditions);
        assertEquals(expectedConditions.size(), actualConditions.size());
        assertEquals(expectedConditions.get(0), actualConditions.get(0));
        assertEquals(expectedConditions.get(1), actualConditions.get(1));
        assertEquals(expectedConditions.get(2), actualConditions.get(2));

        String expectedQueryString = "WHERE e.description LIKE :descr AND e.scheduledDate =:startDate AND e.user.userID =:userID";
        System.out.println(query.toString());
        assertEquals(expectedQueryString, query.toString());
    }

    @Test
    public void shouldAddDescriptionAndStartDateAndEndDateConditions(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "test", null, null, LocalDate.of(2024, 5, 4), LocalDate.of(2024, 5, 15), null, null, null,null);
        List<String> expectedConditions = List.of("WHERE", "e.description LIKE :descr", "AND e.scheduledDate BETWEEN :startDate AND :endDate", "AND e.user.userID=:userID");
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        System.out.println(actualConditions);
        assertNotNull(actualConditions);
        assertEquals(expectedConditions.size(), actualConditions.size());
        assertEquals(expectedConditions.get(0), actualConditions.get(0));
        assertEquals(expectedConditions.get(1), actualConditions.get(1));
        assertEquals(expectedConditions.get(2), actualConditions.get(2));

        String expectedQueryString = "WHERE e.description LIKE :descr AND e.scheduledDate BETWEEN :startDate AND :endDate AND e.user.userID =:userID";
        assertEquals(expectedQueryString, query.toString());
    }

    @Test
    public void shouldAddDescriptionAndStatusAndStartAndEndDateConditions(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "test", null, null, LocalDate.of(2024, 5, 4), LocalDate.of(2024, 5, 15), null, null, TransactionStatus.PENDING, null);
        List<String> expectedConditions = List.of("WHERE", "e.description LIKE :descr", "AND e.scheduledDate BETWEEN :startDate AND :endDate", "AND e.status =:status", "AND e.user.userID=:userID");
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        assertNotNull(actualConditions);
        assertEquals(expectedConditions.size(), actualConditions.size());
        assertEquals(expectedConditions.get(0), actualConditions.get(0));
        assertEquals(expectedConditions.get(1), actualConditions.get(1));
        assertEquals(expectedConditions.get(2), actualConditions.get(2));

        System.out.println(query.toString());
        String expectedQueryString = "WHERE e.description LIKE :descr AND e.scheduledDate BETWEEN :startDate AND :endDate AND e.status =:status AND e.user.userID =:userID";
        assertEquals(expectedQueryString, query.toString());
    }

    @Test
    public void shouldAddDescriptionAndStartAndEndDateAndMinAmountConditions(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "test", new BigDecimal("100.00"), null, LocalDate.of(2024, 5, 4), LocalDate.of(2024, 5, 15), null, null, null,null);
        List<String> expectedConditions = List.of("WHERE", "e.description LIKE :descr", "AND e.scheduledDate BETWEEN :startDate AND :endDate", "AND e.amount := minAmount", "AND e.user.userID=:userID");
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        System.out.println(actualConditions);
        //System.out.println(actualConditions);
        assertNotNull(actualConditions);
        assertEquals(expectedConditions.size(), actualConditions.size());
        assertEquals(expectedConditions.get(0), actualConditions.get(0));
        assertEquals(expectedConditions.get(1), actualConditions.get(1));
        assertEquals(expectedConditions.get(2), actualConditions.get(2));

        System.out.println(query.toString());
        String expectedQuery = "WHERE e.description LIKE :descr AND e.scheduledDate BETWEEN :startDate AND :endDate AND e.amount =:minAmount AND e.user.userID =:userID";
        assertEquals(expectedQuery, query.toString());
    }

    @Test
    public void shouldAddDescriptionAndStartAndEndDateAndMinAndMaxAmountConditions(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "test", new BigDecimal("100.00"), new BigDecimal("500.00"), LocalDate.of(2024, 5, 4), LocalDate.of(2024, 5, 15), null, null, null,null);
        List<String> expectedConditions = List.of("WHERE", "e.description LIKE :descr", "AND e.scheduledDate BETWEEN :startDate AND :endDate", "AND (e.amount >= :minAmount AND e.amount <= :maxAmount)", "AND e.user.userID=:userID");
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        System.out.println(actualConditions);
        assertNotNull(actualConditions);
        assertEquals(expectedConditions.size(), actualConditions.size());
        assertEquals(expectedConditions.get(0), actualConditions.get(0));
        assertEquals(expectedConditions.get(1), actualConditions.get(1));
        assertEquals(expectedConditions.get(2), actualConditions.get(2));

        System.out.println(query.toString());
        String expectedQuery = "WHERE e.description LIKE :descr AND e.scheduledDate BETWEEN :startDate AND :endDate AND (e.amount >= :minAmount AND e.amount <= :maxAmount) AND e.user.userID =:userID";
        assertEquals(expectedQuery, query.toString());
    }

    @Test
    public void shouldAddStartAndEndDateAndMinAndMaxAmountConditions(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "", new BigDecimal("100.00"), new BigDecimal("500.00"), LocalDate.of(2024, 5, 4), LocalDate.of(2024, 5, 15), null, null, null,null);
        List<String> expectedConditions = List.of("WHERE", "e.scheduledDate BETWEEN :startDate AND :endDate", "AND (e.amount >= :minAmount AND e.amount <= :maxAmount)", "AND e.user.userID=:userID");
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        System.out.println(actualConditions);
        assertNotNull(actualConditions);
        assertEquals(expectedConditions.size(), actualConditions.size());
        assertEquals(expectedConditions.get(0), actualConditions.get(0));
        assertEquals(expectedConditions.get(1), actualConditions.get(1));
        assertEquals(expectedConditions.get(2), actualConditions.get(2));

        String expectedQuery = "WHERE e.scheduledDate BETWEEN :startDate AND :endDate AND (e.amount >= :minAmount AND e.amount <= :maxAmount) AND e.user.userID =:userID";
        assertEquals(expectedQuery, query.toString());
    }

    @Test
    public void shouldAddStartDateAndMaxAmountConditions(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "", null, new BigDecimal("500.000"), LocalDate.of(2024, 5, 4), null, null, null, null, null);
        List<String> expectedConditions = List.of("WHERE", "e.scheduledDate =:startDate", "AND e.amount =:maxAmount", "AND userID=:userID");
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);

        assertNotNull(actualConditions);
        assertEquals(expectedConditions.size(), actualConditions.size());
        assertEquals(expectedConditions.get(0), actualConditions.get(0));
        assertEquals(expectedConditions.get(1), actualConditions.get(1));
        assertEquals(expectedConditions.get(2), actualConditions.get(2));

        String expectedQuery = "WHERE e.scheduledDate =:startDate AND e.amount =:maxAmount AND e.user.userID =:userID";
        assertEquals(expectedQuery.toString(), query.toString());
    }

    @Test
    public void shouldAddMinAmountAndStatusConditions(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "", new BigDecimal("120.00"), null, null, null,null,null,  TransactionStatus.PENDING,null);
        List<String> expectedConditions = List.of("WHERE", "e.amount =:minAmount", "AND e.status =:status", "AND e.user.userID=:userID");
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);

        assertNotNull(actualConditions);
        assertEquals(expectedConditions.size(), actualConditions.size());
        assertEquals(expectedConditions.get(0), actualConditions.get(0));
        assertEquals(expectedConditions.get(1), actualConditions.get(1));
        assertEquals(expectedConditions.get(2), actualConditions.get(2));

        String expectedQuery = "WHERE e.amount =:minAmount AND e.status =:status AND e.user.userID =:userID";
        assertEquals(expectedQuery.toString(), query.toString());
    }

    @Test
    public void testGetQueryFromHistoryCriteria(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "", new BigDecimal("120.00"), null, null, null, null, null, TransactionStatus.PENDING, TransactionType.WITHDRAW);

        String expectedQuery = "SELECT e FROM WithdrawEntity e WHERE e.amount =:minAmount AND e.status =:status AND e.user.userID =:userID";
        String actualQuery = queryBuilder.getQueryFromHistoryCriteria(historyCriteria);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetQueryFromHistoryCriteria_DescriptionAndStartAndEndDateDepositTable(){
        HistoryCriteria historyCriteria = new HistoryCriteria(1, "test", null, null, LocalDate.of(2024, 5, 3), LocalDate.of(2024, 5, 15), null, null, null, TransactionType.DEPOSIT);

        String expectedQuery = "SELECT e FROM DepositsEntity e WHERE e.description LIKE :descr AND e.scheduledDate BETWEEN :startDate AND :endDate AND e.user.userID =:userID";
        String actualQuery = queryBuilder.getQueryFromHistoryCriteria(historyCriteria);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testRevisedOnlyDescription(){
        when(historyCriteria.description()).thenReturn("test");

        List<String> conditions = new ArrayList<>();
        List<String> expectedConditions = List.of("WHERE ", "e.description LIKE :descr", " AND e.user.userID=:userID");
        StringBuilder query = new StringBuilder();

        queryBuilder.revised(historyCriteria, conditions, query);

        assertEquals(3, conditions.size());
        assertEquals(expectedConditions.get(0), conditions.get(0));
        assertEquals(expectedConditions.get(1), conditions.get(1));
        assertEquals("e.description LIKE :descr", conditions.get(1));
    }

    @Test
    public void testRevisedDescriptionAndStatus(){
        when(historyCriteria.description()).thenReturn("test");
        when(historyCriteria.status()).thenReturn(TransactionStatus.PENDING);

        List<String> conditions = new ArrayList<>();
        List<String> expectedConditions = List.of("WHERE ", "e.description LIKE :descr", " AND e.status=:status", " AND e.user.userID=:userID");
        StringBuilder query = new StringBuilder();

        queryBuilder.revised(historyCriteria, conditions, query);

        System.out.println(conditions);
        assertEquals(4, conditions.size());
        for(int i = 0; i < conditions.size(); i++){
            assertEquals(expectedConditions.get(i), expectedConditions.get(i));
        }

        assertEquals("e.description LIKE :descr", conditions.get(1));
    }


    @AfterEach
    void tearDown() {
    }
}