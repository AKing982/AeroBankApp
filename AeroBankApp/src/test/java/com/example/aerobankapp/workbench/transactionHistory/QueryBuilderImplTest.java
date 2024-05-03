package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.exceptions.NullHistoryCriteriaException;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class QueryBuilderImplTest {

    private QueryBuilder queryBuilder;

    private StringBuilder query;

    @BeforeEach
    void setUp() {
        queryBuilder = new QueryBuilderImpl();
        query = new StringBuilder();
    }

    @Test
    public void testBuildQueryConditions_NullCriteria(){
        assertThrows(NullHistoryCriteriaException.class, () -> {
            queryBuilder.buildQueryConditions(null, query);
        });
    }

    @Test
    public void testShouldReturnEmptyConditionsForEmptyCriteria(){
        HistoryCriteria historyCriteria = new HistoryCriteria(null, null, null, null, null, null, null);
        List<String> conditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        assertTrue(conditions.isEmpty());
        assertTrue(query.toString().isEmpty());
    }

    @Test
    public void shouldAddDescriptionCondition(){
        HistoryCriteria historyCriteria = new HistoryCriteria("test", null, null, null, null, null, null);
        List<String> conditions = queryBuilder.buildQueryConditions(historyCriteria, query);
        assertTrue(conditions.contains("e.description LIKE :descr"),"Conditions should contain description condition");
        assertTrue(query.toString().contains("e.description LIKE :descr"));
    }

    @Test
    void shouldAppendWhereToQueryIfConditionsExist() {
        HistoryCriteria criteria = new HistoryCriteria("test", null, null, null, null, null, null);
        queryBuilder.buildQueryConditions(criteria, query);
        assertTrue(query.toString().startsWith("WHERE"), "Query should start with WHERE when conditions exist");
    }


    @Test
    public void testBuildQueryConditions_DescriptionAndElseBlank(){
        StringBuilder query = new StringBuilder();
        HistoryCriteria historyCriteria = new HistoryCriteria("Test", null, null, null, null, null, null);

        // Expected condition strings
        List<String> expectedConditions = List.of("WHERE", "e.description LIKE :descr");
        // Build actual conditions
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);

        // Check conditions
        assertNotNull(actualConditions, "Condition list should not be null");
        assertEquals(expectedConditions.size(), actualConditions.size(), "Condition size should match expected");
        assertEquals(expectedConditions.get(0), actualConditions.get(0), "First condition should be WHERE");
        assertEquals(expectedConditions.get(1), actualConditions.get(1), "Second condition should be the description LIKE clause");

        // Check the actual query string built
        String expectedQueryString = "WHERE e.description LIKE :descr";
        assertEquals(expectedQueryString, query.toString(), "Query string should contain WHERE clause and description condition");
    }

    @Test
    public void shouldAddDescriptionAndStartDateConditions(){

        HistoryCriteria historyCriteria = new HistoryCriteria("test", null, null, LocalDate.of(2024, 5, 5), null, null, null);

        List<String> expectedConditions = List.of("WHERE", "e.description LIKE :descr", "AND e.scheduledDate :startDate");
        List<String> actualConditions = queryBuilder.buildQueryConditions(historyCriteria, query);

        assertNotNull(actualConditions);
        assertEquals(expectedConditions.size(), actualConditions.size());
        assertEquals(expectedConditions.get(0), actualConditions.get(0));
        assertEquals(expectedConditions.get(1), actualConditions.get(1));
        assertEquals(expectedConditions.get(2), actualConditions.get(2));

        String expectedQueryString = "WHERE e.description LIKE :descr AND e.scheduledDate :startDate";
        assertEquals(expectedQueryString, query.toString());
    }

    @AfterEach
    void tearDown() {
    }
}