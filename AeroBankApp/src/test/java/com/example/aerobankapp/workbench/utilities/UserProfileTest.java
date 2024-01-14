package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.account.AccountNumber;
import com.example.aerobankapp.account.AccountPrefix;
import com.example.aerobankapp.model.CheckingAccountModel;
import com.example.aerobankapp.services.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class UserProfileTest {

    @MockBean
    private UserProfile userProfile;

    @Autowired
    private UserProfileFacade userProfileFacade;

    private String mockUser;

    @Autowired
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testConstructorForNull() {
        assertThrows(NullPointerException.class,
                () -> userProfile = new UserProfile(null));
    }

    @Test
    public void testConstructorWithValidUserName() {
        mockUser = "AKing94";
        userProfile = new UserProfile(mockUser);
        userProfile.setUserProfileFacade(userProfileFacade);

        assertEquals(mockUser, userProfile.getUsername());
    }

    @Test
    public void testAddingAccountNumbers()
    {
        mockUser = "AKing94";
        userProfile = new UserProfile(mockUser);
        userProfile.setUserProfileFacade(userProfileFacade);
        AccountPrefix prefix = new AccountPrefix("CA");

        userProfile.addAccountNumbers(new AccountNumber(prefix, "4343", "54545"));
        userProfile.addAccountNumbers(new AccountNumber(prefix, "6767", "23232"));

        assertEquals(2, userProfile.getAccountNumbers().size());
    }

    @Test
    public void testDistinctUserProfilesWithDistinctUserNames()
    {
        String bsmith = "BSmith23";
        String aking = "AKing94";

        UserProfile akingProfile = new UserProfile(aking);
        akingProfile.setUserProfileFacade(userProfileFacade);

        UserProfile bsmithProfile = new UserProfile(bsmith);
        bsmithProfile.setUserProfileFacade(userProfileFacade);

        assertNotNull(akingProfile);
        assertNotNull(bsmithProfile);
        assertEquals(aking, akingProfile.getUsername());
        assertEquals(bsmith, bsmithProfile.getUsername());
        assertNotEquals(akingProfile.getUsername(), bsmithProfile.getUsername());
        assertNotEquals(akingProfile, bsmithProfile);
        assertNotEquals(akingProfile.getAllCheckingAccounts(), bsmithProfile.getAllCheckingAccounts());
    }

    @Test
    public void testAllAccountNumbers()
    {
        mockUser = "AKing94";
        userProfile = new UserProfile(mockUser);


        List<AccountNumber> accountNumberList = userProfile.getAllAccountNumbers();

        assertEquals(0, accountNumberList.size());
    }

    @AfterEach
    void tearDown()
    {
        mockUser = "";
        userProfile = null;
    }
}