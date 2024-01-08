package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.workbench.utilities.Role;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
class UserSecurityProfileTest {

    @MockBean
    private UserSecurityProfile userSecurityProfile;

    private Role role;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    public void testConstructorWithNullRoleShouldThrowException()
    {
        assertThrows(NullPointerException.class,
                () -> userSecurityProfile = new UserSecurityProfile(null));
    }

    @AfterEach
    void tearDown() {
    }
}