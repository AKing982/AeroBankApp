package com.example.aerobankapp.workbench;

import com.example.aerobankapp.workbench.transactions.TransactionType;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(JUnit38ClassRunner.class)
class DescriptionCodeImplTest {
    private DescriptionCodeImpl descriptionCode;
    private String description = "Transfer to Checking";
    private String withdrawDescription = "Withdraw from Checking";
    private String accountCode = "A1";
    private TransactionType transactionType;
    private LocalDate depositDate = LocalDate.now();

    @BeforeEach
    void setUp()
    {
        descriptionCode = new DescriptionCodeImpl(TransactionType.DEPOSIT, description, accountCode, depositDate);
    }

    @Test
    public void testDepositDescriptionCode_NullInput(){

        assertThrows(NullPointerException.class, () -> {
            DescriptionCodeImpl descriptionCode1 = new DescriptionCodeImpl(null, null, null, null);
            descriptionCode1.build();
        });
    }

    @Test
    public void testDepositDescriptionCode_DepositCategory(){
        String expectedCode = "DEPOSIT - Transfer to Checking - A1 - " + LocalDate.now().toString();

        String actualCode = descriptionCode.build();

        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testWithdrawDescriptionCode_WithdrawCategory(){
        DescriptionCode descriptionCode = new DescriptionCodeImpl(TransactionType.WITHDRAW, withdrawDescription, accountCode, LocalDate.now());
        String expectedCode = "WITHDRAW - Withdraw from Checking - A1 - " + LocalDate.now();
        String actualCode = descriptionCode.build();

        assertEquals(expectedCode, actualCode);
    }

    @AfterEach
    void tearDown() {
    }
}