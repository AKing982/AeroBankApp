package com.example.aerobankapp.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AccountIDTest {

    private AccountID accountID;
    private AccountType accountType = AccountType.CHECKING;

    @BeforeEach
    void setUp()
    {
        accountID = new AccountID(accountType);
    }

    @Test
    public void createIDA1()
    {
        String id = "A1";
        String acctID = accountID.buildID();

        assertEquals(id, acctID);
    }

    @AfterEach
    void tearDown()
    {

    }
}