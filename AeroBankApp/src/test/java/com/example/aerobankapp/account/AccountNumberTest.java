package com.example.aerobankapp.account;

import org.assertj.core.util.Streams;
import org.checkerframework.checker.units.qual.A;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnit38ClassRunner.class)
class AccountNumberTest {

    private AccountNumber accountNumber;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    public void testConstructor()
    {
        accountNumber = new AccountNumber(null, null, null);

        AccountPrefix prefix = accountNumber.getAccountPrefix();
        String branch = accountNumber.getBranchIdentifier();
        String account = accountNumber.getAccountNum();

        assertNull(prefix);
        assertNull(branch);
        assertNull(account);
    }

    @Test
    @DisplayName("Test Null AccountPrefix")
    public void testNullAccountPrefix()
    {
        assertNull(accountNumber);
        assertThrows(IllegalArgumentException.class,
                () -> {AccountPrefix prefix = new AccountPrefix(null);});
    }

    @Test
    @DisplayName("Test Checking Account Number")
    public void testCheckingAccountNumber()
    {
        AccountPrefix checking = new AccountPrefix("CA");
        String branch = "2323";
        String account = "56567";
        String expected = "CA-2323-56567";
        AccountNumber accountNumber1 = new AccountNumber(checking, branch, account);
        String actualAccountNumber = accountNumber1.build();

        assertEquals(expected, actualAccountNumber);
    }

    @Test
    @DisplayName("Test Checking Account Number with Builder")
    public void testCheckingAccountNumberWithBuilder()
    {
        AccountPrefix checking = new AccountPrefix("CA");
        String branch = "2323";
        String account = "565657";

        AccountNumber accountNumber1 = AccountNumber.builder()
                .accountPrefix(checking)
                .accountNum(account)
                .branchIdentifier(branch)
                .build();

        assertNotNull(accountNumber1);
    }

    @ParameterizedTest
    @MethodSource("provideValuesForTesting")
    @DisplayName("Test AccountNumber values")
    public void testAccountNumberValues(AccountPrefix prefix, String branch, String account, String result)
    {
        AccountNumber accountNumber1 = new AccountNumber(prefix, branch, account);

        assertEquals(accountNumber1.build(), result);
    }

    private static Stream<Arguments> provideValuesForTesting()
    {
        return Stream.of(
                Arguments.of("CA", "5553", "233", "CA-5553-233"),
                Arguments.of("CA", "5553", "23232", "CA-5553-23232"),
                Arguments.of("SA", "4758", "47050", "SA-4758-47050"),
                Arguments.of("IA", "443", "232", "IA-443-232"),
                Arguments.of("SAS", "4444", "23232", "SAS-443-232")
        );
    }

    @Test
    @DisplayName("Test Invalid Checking Account Number with incorrect parameters")
    public void testInvalidCheckingAccountNumberBadParameters()
    {
        AccountPrefix checking = new AccountPrefix("CA");
        String branch = "223";
        String account = "34";
        AccountNumber accountNumber1 = AccountNumber.builder()
                        .accountPrefix(checking)
                        .accountNum(account)
                        .branchIdentifier(branch)
                        .build();

        assertThrows(IllegalArgumentException.class,
                () -> {accountNumber1.build();});


    }

    @AfterEach
    void tearDown() {
    }
}