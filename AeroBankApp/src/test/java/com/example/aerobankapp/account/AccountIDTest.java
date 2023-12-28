package com.example.aerobankapp.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class GenerateAccountIDTest {

    private GenerateAccountID accountID;
    private AccountType accountType = AccountType.CHECKING;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testCreatingAccountIDConstructor() {
        final AccountType checking = AccountType.CHECKING;
        final String name = "Alex";
        accountID = new GenerateAccountID(checking, name);

        String actualName = accountID.getName();
        AccountType actualAccountType = accountID.getAccountType();

        assertNotNull(actualName);
        assertNotNull(actualAccountType);
        assertNotNull(accountID);
    }

    @Test
    public void testAccountIDConstructorWithNullValues() {
        final AccountType nullType = null;
        final String nullName = null;

        accountID = new GenerateAccountID(nullType, nullName);

        assertThrows(NullPointerException.class,
                () -> {
                    accountID.buildID();
                });
    }

    @Test
    public void testBuildingAccountIDA1() {
        final AccountType checking = AccountType.CHECKING;
        final String firstName = "Alex";

        accountID = new GenerateAccountID(checking, firstName);
        String expectedID = "A1";
        String actualID = accountID.buildID();

        assertEquals(expectedID, actualID);
    }

    @Test
    public void testBuildingAccountIDA2() {
        final AccountType savings = AccountType.SAVINGS;
        final String firstName = "Alex";

        accountID = new GenerateAccountID(savings, firstName);

        String expectedID = "A2";
        String actualID = accountID.buildID();

        assertEquals(expectedID, actualID);
    }

    @Test
    public void testBuildingAccountIDA3() {
        final AccountType rent = AccountType.RENT;
        final String firstName = "Alex";

        accountID = new GenerateAccountID(rent, firstName);

        String expected = "A3";
        String actual = accountID.buildID();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @DisplayName("Test First Name constructor values")
    @MethodSource("provideMethodValuesForTesting")
    public void testAccountIDValues(AccountType accountType, String firstName, String result) {
        accountID = new GenerateAccountID(accountType, firstName);

        assertEquals(result, accountID.buildID());
    }

    private static Stream<Arguments> provideMethodValuesForTesting() {
        return Stream.of(
                Arguments.of(AccountType.CHECKING, "Alex", "A1"),
                Arguments.of(AccountType.CHECKING, "Bob", "B1"),
                Arguments.of(AccountType.CHECKING, "Smith", "S1"),
                Arguments.of(AccountType.CHECKING, "Sam", "S1"),
                Arguments.of(AccountType.SAVINGS, "Bob", "B2"),
                Arguments.of(AccountType.SAVINGS, "Sam", "S2"),
                Arguments.of(AccountType.SAVINGS, "Smith", "S2"),
                Arguments.of(AccountType.INVESTMENT, "Alex", "A5"),
                Arguments.of(AccountType.RENT, "Alex", "A3"),
                Arguments.of(AccountType.MORTGAGE, "Alex", "A4"),
                Arguments.of(AccountType.MORTGAGE, "Bob", "B4"),
                Arguments.of(AccountType.MORTGAGE, "Smith", "S4"),
                Arguments.of(AccountType.RENT, "", "A3"),
                Arguments.of(null, " Alex", "A5"),
                Arguments.of(AccountType.CHECKING, " Alex King", "A1"),
                Arguments.of(AccountType.CHECKING, "Alex King", "A1")
        );
    }

    @ParameterizedTest
    @DisplayName("Test the Constructor with fullname")
    @MethodSource("provideFullNameConstructorArguments")
    public void testFullNameConstructor(String fullName, AccountType acctType, String result) {
        accountID = new GenerateAccountID(fullName);

        assertEquals(result, accountID.getAccountID(accountType));
    }

    private static Stream<Arguments> provideFullNameConstructorArguments()
    {
        return Stream.of(
                Arguments.of(" Alex King", AccountType.CHECKING, "A1"),
                Arguments.of(" Alex", AccountType.CHECKING, "A1"),
                Arguments.of("", AccountType.CHECKING, "A1")
        );
    }




    @AfterEach
    void tearDown()
    {

    }
}