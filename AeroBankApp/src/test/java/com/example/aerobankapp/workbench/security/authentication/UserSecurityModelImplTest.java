package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
class UserSecurityModelImplTest {

    private UserSecurityModelImpl mockSecurityModel;
    private String mockUser;
    private UserProfile mockProfile;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testConstructorForEmptyUserName()
    {
        mockUser = "";
        assertThrows(IllegalArgumentException.class,
                () -> {mockSecurityModel = new UserSecurityModelImpl(mockUser);});
    }

    @Test
    public void testConstructorWithValidUserName()
    {
        mockUser = "AKing94";
        mockSecurityModel = new UserSecurityModelImpl(mockUser);

        assertNotNull(mockSecurityModel);
        assertEquals(mockUser, mockSecurityModel.getCurrentUserProfile().getUsername());
    }

    @Test
    public void testIsUserEnabled()
    {
        mockUser = "AKing94";
        mockSecurityModel = new UserSecurityModelImpl(mockUser);

        boolean isUserEnabled = mockSecurityModel.isEnabled();

        assertNotNull(mockSecurityModel);
        assertTrue(isUserEnabled);
    }

    @Test
    public void testCheckingAccountDetails()
    {
        mockUser = "AKing94";
        mockSecurityModel = new UserSecurityModelImpl(mockUser);

        Set<CheckingAccountEntity> checkingAccountEntities = mockSecurityModel.getCheckingAccountDetails();

        assertNull(checkingAccountEntities);
        assertEquals(0, checkingAccountEntities.size());
    }



    @AfterEach
    void tearDown()
    {
        mockUser = "";
        mockSecurityModel = null;
    }
}