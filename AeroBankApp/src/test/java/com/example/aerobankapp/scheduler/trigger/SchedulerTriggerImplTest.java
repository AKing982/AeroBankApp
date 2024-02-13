package com.example.aerobankapp.scheduler.trigger;

import com.example.aerobankapp.scheduler.CronExpressionBuilder;
import com.example.aerobankapp.scheduler.CronExpressionBuilderImpl;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.quartz.DateBuilder.dateOf;

@SpringBootTest
@RunWith(SpringRunner.class)
class SchedulerTriggerImplTest
{
    private SchedulerTriggerImpl schedulerTrigger;

    @Mock
    private TriggerCriteria triggerCriteria;


    private CronExpressionBuilder cronExpressionBuilder;

    private TriggerCriteria mockTriggerCriteria;

    @BeforeEach
    void setUp() {


        cronExpressionBuilder = new CronExpressionBuilderImpl(triggerCriteria);
        schedulerTrigger = new SchedulerTriggerImpl(triggerCriteria, cronExpressionBuilder);

   }

   @Test
   public void testConstructorNullCriteria()
   {
       assertThrows(NullPointerException.class, () -> {
           schedulerTrigger = new SchedulerTriggerImpl(null, null);
       });
   }

   @Test
   public void testGetOnceTrigger()
   {
      TriggerCriteria mockTriggerCriteria = TriggerCriteria.builder()
               .day(5)
               .minute(30)
               .hour(7)
               .month(2)
               .year(2024)
               .day(3)
               .interval(ScheduleType.ONCE)
               .second(0)
               .build();

       String triggerID = UUID.randomUUID().toString();
       String groupID = UUID.randomUUID().toString();
       Random random = new Random();
       int randomNumber = random.nextInt(Integer.MAX_VALUE) + 1;

       CronExpressionBuilder cronExpressionBuilder1 = new CronExpressionBuilderImpl(mockTriggerCriteria);

       String onceCronExpression = cronExpressionBuilder1.createCronExpression();

       CronTrigger onceTrigger =  TriggerBuilder.newTrigger()
               .withIdentity(TriggerKey.triggerKey(String.valueOf(randomNumber), String.valueOf(randomNumber)))
               .withSchedule(CronScheduleBuilder.cronSchedule(onceCronExpression))
               .startAt(dateOf(mockTriggerCriteria.getHour(), mockTriggerCriteria.getMinute(), 0, mockTriggerCriteria.getDay(), mockTriggerCriteria.getMonth(), mockTriggerCriteria.getYear()))
               .build();

       SchedulerTriggerImpl schedulerTrigger1 = new SchedulerTriggerImpl(mockTriggerCriteria, cronExpressionBuilder1);
       CronTrigger actualTrigger = schedulerTrigger1.getRunOnceTrigger();

       assertNotNull(schedulerTrigger1);
       assertEquals(onceTrigger.getExpressionSummary(), actualTrigger.getExpressionSummary());
       assertEquals(onceTrigger.getCronExpression(), actualTrigger.getCronExpression());
   }

   @Test
   public void getDailyTrigger()
   {
       TriggerCriteria mockTriggerCriteria = TriggerCriteria.builder()
               .day(5)
               .minute(30)
               .hour(7)
               .month(2)
               .year(2024)
               .day(3)
               .interval(ScheduleType.DAILY)
               .second(0)
               .build();

       Random random = new Random();
       int randomNumber = random.nextInt(Integer.MAX_VALUE) + 1;

       CronExpressionBuilder cronExpressionBuilder1 = new CronExpressionBuilderImpl(mockTriggerCriteria);

       SchedulerTriggerImpl schedulerTrigger1 = new SchedulerTriggerImpl(mockTriggerCriteria, cronExpressionBuilder1);

       String dailyCronExpression = cronExpressionBuilder1.createCronExpression();

       CronTrigger dailyCronTrigger = TriggerBuilder.newTrigger()
               .withIdentity(TriggerKey.triggerKey(String.valueOf(randomNumber), String.valueOf(randomNumber)))
               .withSchedule(CronScheduleBuilder.cronSchedule(dailyCronExpression))
               .build();

       CronTrigger actualDailyTrigger = schedulerTrigger1.getDailyTrigger();

       assertNotNull(actualDailyTrigger);
       assertEquals(dailyCronTrigger.getCronExpression(), actualDailyTrigger.getCronExpression());
       assertEquals(dailyCronTrigger.getExpressionSummary(), actualDailyTrigger.getExpressionSummary());
   }

   @Test
   public void testGetDailyTwoDayTrigger()
   {

       TriggerCriteria mockTriggerCriteria = TriggerCriteria.builder()
               .day(5)
               .minute(30)
               .hour(7)
               .month(2)
               .year(2024)
               .day(3)
               .interval(ScheduleType.BIDAILY)
               .second(0)
               .build();

       Random random = new Random();
       int randomNumber = random.nextInt(Integer.MAX_VALUE) + 1;

       CronExpressionBuilder cronExpressionBuilder1 = new CronExpressionBuilderImpl(mockTriggerCriteria);

       SchedulerTriggerImpl schedulerTrigger1 = new SchedulerTriggerImpl(mockTriggerCriteria, cronExpressionBuilder1);

       Calendar startTime = Calendar.getInstance();
       startTime.set(Calendar.HOUR_OF_DAY, mockTriggerCriteria.getHour());
       startTime.set(Calendar.MINUTE, mockTriggerCriteria.getMinute());
       startTime.set(Calendar.SECOND, 0);
       startTime.set(Calendar.MILLISECOND, 0);

       // Define the interval: 48 hours for every two days
       long intervalInHours = 48L;

       SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger()
               .withIdentity(TriggerKey.triggerKey(String.valueOf(randomNumber), String.valueOf(randomNumber)))
               .startAt(startTime.getTime())
               .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                       .withIntervalInHours((int) intervalInHours)
                       .repeatForever())
               .build();

       SimpleTrigger actualTrigger = schedulerTrigger1.getDailyTwoDayTrigger();

       assertEquals(simpleTrigger.getRepeatInterval(), actualTrigger.getRepeatInterval());
       assertEquals(simpleTrigger.getStartTime(), actualTrigger.getStartTime());
       assertEquals(startTime.getTime(), actualTrigger.getStartTime());
       assertEquals(simpleTrigger.getNextFireTime(), actualTrigger.getNextFireTime());
   }

   @Test
   public void testGetWeeklyTrigger()
   {
       TriggerCriteria mockTriggerCriteria = TriggerCriteria.builder()
               .day(5)
               .minute(30)
               .hour(7)
               .month(2)
               .year(2024)
               .day(3)
               .interval(ScheduleType.WEEKLY)
               .second(0)
               .build();

       Random random = new Random();
       int randomNumber = random.nextInt(Integer.MAX_VALUE) + 1;

       CronExpressionBuilder cronExpressionBuilder1 = new CronExpressionBuilderImpl(mockTriggerCriteria);

       String weeklyExpression = cronExpressionBuilder1.createCronExpression();

       SchedulerTriggerImpl schedulerTrigger1 = new SchedulerTriggerImpl(mockTriggerCriteria, cronExpressionBuilder1);

       CronTrigger weeklyTrigger = TriggerBuilder.newTrigger()
               .withIdentity("weeklyTrigger", "group1")
               .withSchedule(CronScheduleBuilder.cronSchedule(weeklyExpression))
               .build();

       CronTrigger actualTrigger = schedulerTrigger1.getWeeklyTrigger();

       assertEquals(weeklyTrigger.getExpressionSummary(), actualTrigger.getExpressionSummary());


   }

    @AfterEach
    void tearDown() {
    }
}