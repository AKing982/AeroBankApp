package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.model.CheckingAccount;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class UserProfileTest {

    private UserProfile userProfile;

    @BeforeEach
    void setUp()
    {
        userProfile = new UserProfile("AKing94");
    }

    @Test
    public void testUserProfileConstructor()
    {
        assertNotNull(userProfile);
    }

    @Test
    public void testUserProfileUserName()
    {
        String user = "AKing94";
        String actualUser = userProfile.getUsername();

        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

    @Test
    public void testLoadingCheckingAccounts()
    {
        List<CheckingAccount> checkingAccountList = Arrays.asList(new CheckingAccount(), new CheckingAccount());
        List<CheckingAccount> actualCheckingAccounts = userProfile.getCheckingAccounts();

        assertTrue(actualCheckingAccounts.size() > 1);
        assertNotEquals(checkingAccountList, actualCheckingAccounts);
    }

    @AfterEach
    void tearDown() {
    }
}