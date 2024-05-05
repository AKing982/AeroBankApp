package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.exceptions.NullHistoryCriteriaException;
import com.example.aerobankapp.model.SQLCondition;
import com.example.aerobankapp.model.SQLOperand;
import com.example.aerobankapp.model.SQLSelect;
import com.example.aerobankapp.model.SQLTable;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import com.example.aerobankapp.workbench.utilities.TransferType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryBuilderImplTest {

    private QueryBuilderImpl queryBuilder;

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
    public void testGetDefaultUserQuery(){
        String expected = "SELECT e FROM DepositsEntity e WHERE e.user.userID=:userID";
        String actual = queryBuilder.getDefaultUserQuery();

        assertEquals(expected, actual);
    }

    @Test
    public void testBuildConditionsStatements_DescriptionAndStartDate(){
        when(historyCriteria.description()).thenReturn("test");
        when(historyCriteria.status()).thenReturn(TransactionStatus.PENDING);

        SQLOperand expectedWhere = new SQLOperand("AND");

        expectedWhere.addComponent(new SQLCondition("e.description", "LIKE", ":descr"));
        expectedWhere.addComponent(new SQLCondition("e.status", "=", ":status"));

        SQLOperand actualWhere = new SQLOperand("AND");
        queryBuilder.buildConditionsStatementsFromCriteria(historyCriteria, actualWhere);
        System.out.println(actualWhere.buildQueryComponent());
        assertEquals(expectedWhere.buildQueryComponent(), actualWhere.buildQueryComponent());
    }

    @Test
    public void testBuildConditionStatements_DescriptionAndStatusAndStartDate(){
        when(historyCriteria.description()).thenReturn("test");
        when(historyCriteria.status()).thenReturn(TransactionStatus.PENDING);
        when(historyCriteria.startDate()).thenReturn(LocalDate.now());

        SQLOperand expectedWhere = new SQLOperand("AND");
        expectedWhere.addComponent(new SQLCondition("e.description", "LIKE ", ":descr"));
        expectedWhere.addComponent(new SQLCondition("e.scheduledDate", "=", ":startDate"));
        expectedWhere.addComponent(new SQLCondition("e.status", "=", ":status"));

        SQLOperand actualWhere = new SQLOperand("AND");

        queryBuilder.buildConditionsStatementsFromCriteria(historyCriteria, actualWhere);
        System.out.println(actualWhere.buildQueryComponent());
        assertEquals(expectedWhere.buildQueryComponent(), actualWhere.buildQueryComponent());
    }

    @Test
    public void testBuildConditionStatements_StatusAndStartAndEndDate(){
        when(historyCriteria.status()).thenReturn(TransactionStatus.PENDING);
        when(historyCriteria.startDate()).thenReturn(LocalDate.now());
        when(historyCriteria.endDate()).thenReturn(LocalDate.of(2024, 5, 10));

        SQLOperand expectedWhere = new SQLOperand("AND");
        expectedWhere.addComponent(new SQLCondition("e.status", "=", ":status"));
        expectedWhere.addComponent(new SQLCondition("e.scheduledDate", "BETWEEN", ":startDate AND :endDate"));

        SQLOperand actualWhere = new SQLOperand("AND");
        queryBuilder.buildConditionsStatementsFromCriteria(historyCriteria, actualWhere);
        System.out.println(actualWhere.buildQueryComponent());
        assertEquals(expectedWhere.buildQueryComponent(), actualWhere.buildQueryComponent());
    }

    @Test
    public void testBuildDefaultUserClause(){
        SQLOperand sqlOperand = new SQLOperand("");
        sqlOperand.addComponent(new SQLCondition("e.user.userID", "=", ":userID"));

        SQLOperand actual = queryBuilder.buildDefaultUserClause();
        System.out.println(actual.buildQueryComponent());
        assertEquals(sqlOperand.buildQueryComponent(), actual.buildQueryComponent());
    }

    @Test
    public void testBuildSQLQueryDefaultUser(){
        SQLOperand expectedWhere = new SQLOperand("");
        expectedWhere.addComponent(new SQLCondition("e.user.userID", "=", ":userID"));

        SQLSelect expectedSelect = new SQLSelect();
        expectedSelect.addSelection("e");
        SQLTable expectedTable = new SQLTable("DepositsEntity", "e");

        String expectedQuery = "SELECT e FROM DepositsEntity e WHERE e.user.userID =:userID";

        String actualQuery = queryBuilder.buildSQLQuery(expectedSelect, expectedTable, expectedWhere);
        System.out.println(actualQuery);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetQueryFromCriteria_DescriptionAndStatus(){
        when(historyCriteria.description()).thenReturn("test");
        when(historyCriteria.status()).thenReturn(TransactionStatus.PENDING);
        when(historyCriteria.transactionType()).thenReturn(TransactionType.DEPOSIT);
        when(historyCriteria.userID()).thenReturn(1);

        String expectedQuery = "SELECT e FROM DepositsEntity e WHERE e.description LIKE :descr AND e.status =:status AND e.user.userID =:userID";

        String actualQuery = queryBuilder.getQueryFromCriteria(historyCriteria);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void getQueryFromCriteria_StatusAndMinAmountAndMaxAmount(){
        when(historyCriteria.status()).thenReturn(TransactionStatus.PENDING);
        when(historyCriteria.minAmount()).thenReturn(new BigDecimal("120.000"));
        when(historyCriteria.maxAmount()).thenReturn(new BigDecimal("250.000"));
        when(historyCriteria.transactionType()).thenReturn(TransactionType.DEPOSIT);
        when(historyCriteria.userID()).thenReturn(1);

        String expectedQuery = "SELECT e FROM DepositsEntity e WHERE e.status =:status AND (e.amount >=:minAmount AND e.amount <=:maxAmount) AND e.user.userID =:userID";
        String actualQuery = queryBuilder.getQueryFromCriteria(historyCriteria);

        System.out.println(actualQuery);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void getQueryFromCriteria_StatusAndMinAmount(){
        when(historyCriteria.status()).thenReturn(TransactionStatus.PENDING);
        when(historyCriteria.minAmount()).thenReturn(new BigDecimal("120.000"));
        when(historyCriteria.transactionType()).thenReturn(TransactionType.DEPOSIT);
        when(historyCriteria.userID()).thenReturn(1);

        String expectedQuery = "SELECT e FROM DepositsEntity e WHERE e.status =:status AND e.amount =:minAmount AND e.user.userID =:userID";
        String actualQuery = queryBuilder.getQueryFromCriteria(historyCriteria);

        System.out.println(actualQuery);

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetQueryFromCriteria_TransferType(){
        when(historyCriteria.transactionType()).thenReturn(TransactionType.DEPOSIT);
        when(historyCriteria.userID()).thenReturn(1);
        when(historyCriteria.transferType()).thenReturn(TransferType.USER_TO_USER);

        String expectedQuery = "SELECT e FROM DepositsEntity e WHERE e.transferType =:transferType AND e.user.userID =:userID";
        String actualQuery = queryBuilder.getQueryFromCriteria(historyCriteria);

        System.out.println(actualQuery);
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetQueryFromCriteria_FullQuery_DepositsTable(){
        when(historyCriteria.description()).thenReturn("test");
        when(historyCriteria.status()).thenReturn(TransactionStatus.PENDING);
        when(historyCriteria.minAmount()).thenReturn(new BigDecimal("120.000"));
        when(historyCriteria.maxAmount()).thenReturn(new BigDecimal("500.00"));
        when(historyCriteria.startDate()).thenReturn(LocalDate.now());
        when(historyCriteria.endDate()).thenReturn(LocalDate.of(2024, 5, 10));
        when(historyCriteria.transferType()).thenReturn(TransferType.USER_TO_USER);
        when(historyCriteria.scheduledTime()).thenReturn(LocalTime.now());
        when(historyCriteria.transactionType()).thenReturn(TransactionType.DEPOSIT);
        when(historyCriteria.userID()).thenReturn(1);

        String expected = "SELECT e FROM DepositsEntity e WHERE e.description LIKE :descr AND e.status =:status AND (e.amount >=:minAmount AND e.amount <=:maxAmount) AND e.scheduledDate BETWEEN :startDate AND :endDate AND e.transferType =:transferType AND e.scheduledTime =:scheduledTime AND e.user.userID =:userID";
        String actual = queryBuilder.getQueryFromCriteria(historyCriteria);

        System.out.println(actual);

        assertEquals(expected, actual);
    }


    @AfterEach
    void tearDown() {
        historyCriteria = null;
    }
}