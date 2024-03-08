package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidSchedulerCriteriaException;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.workbench.utilities.Status;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.aerobankapp.workbench.utilities.SysBuilderUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(JUnit38ClassRunner.class)
class ScheduleParserServiceImplTest
{
    @Autowired
    private ScheduleParserServiceImpl scheduleParserService;

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private SchedulerCriteriaService schedulerCriteriaService;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testGetScheduledCriteriaEntityList_ValidUserID()
    {
        // Arrange
        final int userID = 1;
        List<SchedulerCriteriaEntity> expectedSchedulerCriteriaList = new ArrayList<>();
        SchedulerCriteriaEntity schedulerCriteria = buildSchedulerCriteriaEntity(1, 1, LocalTime.now(), LocalDate.now(), ScheduleType.DAILY);
        expectedSchedulerCriteriaList.add(schedulerCriteria);

        // Act
        schedulerCriteriaService.save(schedulerCriteria);
      //  when(scheduleParserService.getScheduledCriteriaEntityList(userID)).thenReturn(expectedSchedulerCriteriaList);
        List<SchedulerCriteriaEntity> actualSchedulerEntities = scheduleParserService.getScheduledCriteriaEntityList(userID);

        // Assert
        assertNotNull(actualSchedulerEntities);
        assertEquals(expectedSchedulerCriteriaList.size(), actualSchedulerEntities.size());
        assertEquals(expectedSchedulerCriteriaList.get(0).getSchedulerCriteriaID(), actualSchedulerEntities.get(0).getSchedulerCriteriaID());
    }

    @Test
    public void testGetScheduleCriteriaList()
    {
        SchedulerCriteriaEntity schedulerCriteriaEntity1 = buildSchedulerCriteriaEntity(1, 1, LocalTime.now(), LocalDate.now(), ScheduleType.DAILY);
        SchedulerCriteriaEntity schedulerCriteriaEntity2 = buildSchedulerCriteriaEntity(1, 2, LocalTime.now(), LocalDate.now(), ScheduleType.ONCE);
        List<SchedulerCriteriaEntity> schedulerCriteriaEntityList = Arrays.asList(schedulerCriteriaEntity2, schedulerCriteriaEntity1);
        SchedulerCriteria schedulerCriteria = buildSchedulerCriteria(schedulerCriteriaEntity1);
        SchedulerCriteria schedulerCriteria1 = buildSchedulerCriteria(schedulerCriteriaEntity2);
        List<SchedulerCriteria> schedulerCriteriaList = Arrays.asList(schedulerCriteria, schedulerCriteria1);

        List<SchedulerCriteria> actualScheduleCriterias = scheduleParserService.getScheduleCriteriaList(schedulerCriteriaEntityList);

        assertNotNull(actualScheduleCriterias);
        assertEquals(schedulerCriteriaList.size(), actualScheduleCriterias.size());
    }

    @Test
    public void testGetParsedTriggerCriteria()
    {
        // Arrange
        SchedulerCriteria schedulerCriteria = buildSimpleSchedulerCriteria(LocalTime.now(), LocalDate.now(), ScheduleType.ONCE);
        SchedulerCriteria schedulerCriteria1 = buildSimpleSchedulerCriteria(LocalTime.now(), LocalDate.now(), ScheduleType.WEEKLY);
        List<SchedulerCriteria> expectedSchedulerCriteriaList = Arrays.asList(schedulerCriteria, schedulerCriteria1);


        List<TriggerCriteria> triggerCriteriaList = new ArrayList<>();
        TriggerCriteria triggerCriteria = buildTriggerCriteria(ScheduleType.ONCE, 5, 3, 2024, 30, 8, 0);
        TriggerCriteria triggerCriteria1 = buildTriggerCriteria(ScheduleType.ONCE, 2, 4, 2024, 15, 12, 0);
        triggerCriteriaList.add(triggerCriteria);
        triggerCriteriaList.add(triggerCriteria1);

        // Act
     //   when(scheduleParserService.getParsedTriggerCriteria(expectedSchedulerCriteriaList)).thenReturn(triggerCriteriaList);
        List<TriggerCriteria> foundTriggerCriterias = scheduleParserService.getParsedTriggerCriteria(expectedSchedulerCriteriaList);

        assertNotNull(foundTriggerCriterias);
        assertEquals(triggerCriteriaList.size(), foundTriggerCriterias.size());
        for(int i = 0; i < triggerCriteriaList.size(); i++)
        {
            assertEquals(triggerCriteriaList.get(i).getSecond(), foundTriggerCriterias.get(i).getSecond());
            assertEquals(triggerCriteriaList.get(i).getYear(), foundTriggerCriterias.get(i).getYear());
        }
    }

    @Test
    public void testGetParsedTriggerCriteria_EmptyList()
    {
        List<SchedulerCriteria> emptySchedulerCriteria = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> {
            List<TriggerCriteria> actualTriggers = scheduleParserService.getParsedTriggerCriteria(emptySchedulerCriteria);
        });
    }

    @Test
    public void testGetParsedTriggerCriteria_NullCriteria()
    {
        SchedulerCriteria schedulerCriteria = buildSimpleSchedulerCriteria(null, null, ScheduleType.WEEKLY);
        List<SchedulerCriteria> schedulerCriteriaList = Arrays.asList(schedulerCriteria);

        assertThrows(InvalidSchedulerCriteriaException.class, () -> {
            scheduleParserService.getParsedTriggerCriteria(schedulerCriteriaList);
        });
    }

    @AfterEach
    void tearDown() {
    }
}