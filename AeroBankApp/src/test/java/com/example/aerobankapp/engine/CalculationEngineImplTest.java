package com.example.aerobankapp.engine;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.dto.AccountDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class CalculationEngineImplTest {

    private CalculationEngineImpl calculationEngine;


    @BeforeEach
    void setUp()
    {
        calculationEngine = new CalculationEngineImpl();
    }

    @Test
    public void testCalculateDeposit()
    {
        BigDecimal amountToDeposit = new BigDecimal("50.00");
        AccountDTO accountDTO = AccountDTO.builder()
                .accountCode("A1")
                .userID(1)
                .accountID(1)
                .accountName("Alex Checking")
                .accountType(AccountType.CHECKING)
                .isRentAccount(false)
                .balance(new BigDecimal("1510"))
                .build();

        BigDecimal expectedBalance = new BigDecimal("1560.00");
        BigDecimal actualBalance = calculationEngine.calculateDeposit(amountToDeposit, accountDTO);

        assertNotNull(actualBalance);
        assertEquals(expectedBalance, actualBalance);
    }

    @AfterEach
    void tearDown() {
    }
}