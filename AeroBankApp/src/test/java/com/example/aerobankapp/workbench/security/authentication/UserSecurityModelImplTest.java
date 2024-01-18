package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.entity.AccountEntity;
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




    @AfterEach
    void tearDown()
    {
        mockUser = "";
        mockSecurityModel = null;
    }
}