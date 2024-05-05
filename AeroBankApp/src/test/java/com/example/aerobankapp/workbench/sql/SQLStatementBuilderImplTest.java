package com.example.aerobankapp.workbench.sql;

import com.example.aerobankapp.model.SQLOperand;
import com.example.aerobankapp.model.SQLSelect;
import com.example.aerobankapp.model.SQLTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SQLStatementBuilderImplTest {

    private SQLSelect sqlSelect;
    private SQLTable sqlTable;
    private SQLOperand whereConditions;
    private SQLStatementBuilderImpl sqlStatementBuilder;

    @BeforeEach
    public void setUp() {
        sqlSelect = mock(SQLSelect.class);
        sqlTable = mock(SQLTable.class);
        whereConditions = mock(SQLOperand.class);

        when(sqlSelect.buildSQL()).thenReturn("SELECT *");
        when(sqlTable.buildSQL()).thenReturn("FROM table");
        when(whereConditions.buildQueryComponent()).thenReturn("id > 10");

        sqlStatementBuilder = new SQLStatementBuilderImpl(sqlSelect, sqlTable, whereConditions);
    }

    @Test
    public void testBuildQuery_ReturnsCorrectQuery() {
        String expected = "SELECT * FROM table WHERE id > 10";
        String actual = sqlStatementBuilder.buildQuery();
        assertEquals(expected, actual, "The buildQuery method should return the correct query.");
    }

    @Test
    public void testBuildQuery_WithNullSelect_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new SQLStatementBuilderImpl(null, sqlTable, whereConditions);
        });
        assertTrue(exception.getMessage().contains("must not be null"), "Constructor should throw an exception if SQLSelect is null");
    }

    @Test
    public void testBuildQuery_WithNullTable_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new SQLStatementBuilderImpl(sqlSelect, null, whereConditions);
        });
        assertTrue(exception.getMessage().contains("must not be null"), "Constructor should throw an exception if SQLTable is null");
    }

    @Test
    public void testBuildQuery_WithNullWhereConditions_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new SQLStatementBuilderImpl(sqlSelect, sqlTable, null);
        });
        assertTrue(exception.getMessage().contains("must not be null"), "Constructor should throw an exception if SQLOperand is null");
    }

}