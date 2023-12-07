package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.DepositJobDetail;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositSchedulerTest
{
    @MockBean
    private DepositScheduler depositScheduler;

    @Autowired
    @Mock
    private Scheduler scheduler;

    @Autowired
    @Mock
    private SchedulerCriteria schedulerCriteria;

    @Autowired
    @Mock
    private DepositJobDetail depositJobDetail;

    @BeforeEach
    void setUp()
    {
        scheduler = mock(Scheduler.class);
        schedulerCriteria = mock(SchedulerCriteria.class);
        depositJobDetail = mock(DepositJobDetail.class);

        depositScheduler = new DepositScheduler(schedulerCriteria, depositJobDetail);
    }

    @Test
    public void testConstructor() throws SchedulerException {
        Scheduler expectedScheduler = depositScheduler.getScheduler();
        SchedulerCriteria schedulerCriteria1 = depositScheduler.getSchedulerCriteria();
        assertNotNull(depositScheduler);
        assertNotNull(expectedScheduler);
        assertNotNull(schedulerCriteria1);
    }

    @Test
    public void testSchedulerBean() throws SchedulerException
    {
        Scheduler expected = mock(Scheduler.class);
        Scheduler actual = depositScheduler.getSchedulerBean();

        assertNotNull(actual);
        assertNotEquals(expected, actual);
    }

    @Test
    public void testDailySimpleScheduler()
    {
        Scheduler dailyScheduler = depositScheduler.getDailySimpleScheduler();

        assertNotNull(dailyScheduler);
    }

    @AfterEach
    void tearDown()
    {

    }
}