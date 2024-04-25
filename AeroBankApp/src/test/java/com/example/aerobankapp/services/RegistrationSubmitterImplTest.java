package com.example.aerobankapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class RegistrationSubmitterImplTest {

    @InjectMocks
    private RegistrationSubmitterImpl registrationSubmitter;

    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountSecurityService accountSecurityService;

    @Mock
    private AccountPropertiesService accountPropertiesService;



    @BeforeEach
    void setUp() {
        registrationSubmitter = new RegistrationSubmitterImpl(userService, accountService, accountSecurityService, accountPropertiesService);
    }

    @Test
    public void testValidateGeneratedAccountNumber_EmptyAccountNumber(){

    }

    @AfterEach
    void tearDown() {
    }
}