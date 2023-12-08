package com.example.aerobankapp.scheduler.security;

import com.example.aerobankapp.workbench.utilities.UserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SchedulerSecurityImplTest
{
    @MockBean
    private SchedulerSecurityImpl schedulerSecurity;

    @Autowired
    private String user;

    @BeforeEach
    void setUp()
    {
        user = "AKing94";
        schedulerSecurity = new SchedulerSecurityImpl(user);
    }

    @Test
    public void testConstructor()
    {
        UserProfile actualProfile = schedulerSecurity.getUserProfile();
        assertNotNull(actualProfile);
        assertNotNull(schedulerSecurity);
    }

    @Test
    public void testSchedulerUser()
    {
        String expectedUser = "AKing94";
        UserProfile akingProfile = new UserProfile(expectedUser);
        String actualUser = schedulerSecurity.getSchedulerUser();

        assertEquals(expectedUser, actualUser);
        assertNotNull(akingProfile);
    }

    @Test
    public void testScheduleRole()
    {
        ScheduleRole expectedRole = ScheduleRole.SADMIN;
        String user = "AKing94";
        ScheduleRole actualRole = schedulerSecurity.getScheduleRole();

        assertNull(actualRole);
        assertEquals(expectedRole, actualRole);
    }

    @AfterEach
    void tearDown() {
    }
}