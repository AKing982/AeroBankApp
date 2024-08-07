package com.example.aerobankapp.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class TriggerCriteriaEntityTest
{
    private TriggerCriteriaEntity triggerCriteriaEntity;

    @BeforeEach
    void setUp() {
        triggerCriteriaEntity = new TriggerCriteriaEntity();
    }


    @AfterEach
    void tearDown() {
    }
}