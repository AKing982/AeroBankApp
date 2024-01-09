package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.model.CheckingAccountModel;
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

    private String mockUser;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    public void testConstructorForNull()
    {
       assertThrows(IllegalArgumentException.class,
               () -> userProfile = new UserProfile(null));
    }

    @Test
    public void testConstructorWithValidUserName()
    {
        mockUser = "AKing94";
        userProfile = new UserProfile(mockUser);

        assertEquals(mockUser, userProfile.getUsername());
    }

    @AfterEach
    void tearDown()
    {
        mockUser = "";
        userProfile = null;
    }
}