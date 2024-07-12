package com.example.aerobankapp.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SQLOperandTest {

    private SQLOperand sqlOperand;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testEmptyLogicalOperator() {
        SQLOperand op = new SQLOperand("AND");
        System.out.println(op.buildQueryComponent().toString());
        assertEquals("", op.buildQueryComponent());
    }

    @Test
    @Disabled
    public void testSingleCondition() {
        SQLOperand op = new SQLOperand("AND");
        op.addComponent(new SQLCondition("e.age", ">", "18"));
        assertEquals("e.age > 18", op.buildQueryComponent());
    }

    @Test
    @Disabled
    public void testMultipleConditionsAnd() {
        SQLOperand op = new SQLOperand("AND");
        op.addComponent(new SQLCondition("e.age", ">", "18"));
        op.addComponent(new SQLCondition("e.status", "=", "active"));
        assertEquals("e.age > 18 AND e.status = active", op.buildQueryComponent());
    }

    @Test
    @Disabled
    public void testMultipleConditionsOr() {
        SQLOperand op = new SQLOperand("OR");
        op.addComponent(new SQLCondition("e.age", ">", "18"));
        op.addComponent(new SQLCondition("e.status", "=", "active"));
        System.out.println(op.buildQueryComponent().toString());
        assertEquals("e.age > 18 OR e.status = active", op.buildQueryComponent());
    }




    @AfterEach
    void tearDown() {
    }
}