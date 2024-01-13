package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.RoleService;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class UserSecurityProfileTest {

    @MockBean
    private UserSecurityProfile userSecurityProfile;

    @Autowired
    private RoleService roleService;

    private Role role;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    public void testConstructorWithNullRoleShouldThrowException()
    {
        assertThrows(NullPointerException.class,
                () -> userSecurityProfile = new UserSecurityProfile(roleService, null));
    }

    @Test
    public void testConstructorWithValidUserName()
    {

    }




    @AfterEach
    void tearDown() {
    }
}