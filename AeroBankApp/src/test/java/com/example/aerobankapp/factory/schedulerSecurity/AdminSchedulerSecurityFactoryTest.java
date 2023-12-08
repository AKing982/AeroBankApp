package com.example.aerobankapp.factory.schedulerSecurity;

import com.example.aerobankapp.model.SchedulerSecurityDTO;
import com.example.aerobankapp.scheduler.security.ScheduleRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AdminSchedulerSecurityFactoryTest
{
    private AdminSchedulerSecurityFactory adminSchedulerSecurityFactory;

    @BeforeEach
    void setUp()
    {
        adminSchedulerSecurityFactory = new AdminSchedulerSecurityFactory();
    }

    @Test
    public void testAdminSchedulerSecurityFactory()
    {
        assertNotNull(adminSchedulerSecurityFactory);
    }

    @Test
    public void testBuilder()
    {
        SchedulerSecurityDTO builder = adminSchedulerSecurityFactory.createSchedulerSecurity();
        ScheduleRole role = builder.getScheduleRole();
        boolean isScheduleAllowed = builder.isScheduleAllowed();
        boolean isPauseEnabled = builder.isPausedEnabled();
        boolean isCronTriggerEnabled = builder.isCronTriggerEnabled();
        assertNull(builder);
        assertTrue(isScheduleAllowed);
        assertTrue(isPauseEnabled);
        assertTrue(isCronTriggerEnabled);
        assertEquals(ScheduleRole.SADMIN, role);
    }

    @AfterEach
    void tearDown() {
    }
}