package com.example.aerobankapp;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.entity.DepositQueueEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.DepositQueueService;
import com.example.aerobankapp.workbench.utilities.QueueStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositQueueTest {


    private DepositQueue depositQueue;

    @MockBean
    private DepositQueueService queueService;

    @Mock
    private DepositDTO depositDTO;

    @Mock
    private DepositDTO depositDTO1;

    @Mock
    private DepositDTO depositDTO2;

    @BeforeEach
    void setUp()
    {
        depositDTO = mock(DepositDTO.class);
        depositDTO1 = mock(DepositDTO.class);
        depositDTO2 = mock(DepositDTO.class);

        depositQueue = new DepositQueue(queueService);
    }

    @Test
    public void testPollingDepositFromQueue()
    {
        depositQueue.add(depositDTO);

        assertEquals(depositDTO, depositQueue.poll());
        assertEquals(0, depositQueue.size());
    }

    @Test
    public void testAddingSingleDepositToQueue()
    {
        depositQueue.add(depositDTO);


        assertNotNull(depositQueue);
        assertEquals(depositDTO, depositQueue.peek());
        assertEquals(1, depositQueue.size());
    }

    @Test
    public void testAddingMultipleDepositsToQueue()
    {
        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO1);
        depositQueue.add(depositDTO2);

        assertNotNull(depositQueue);

        assertEquals(depositDTO, depositQueue.peek());
        assertEquals(3, depositQueue.size());
    }

    @Test
    public void testAddingListOfDepositsToQueue()
    {
        List<DepositDTO> depositDTOList = new ArrayList<>();
        depositDTOList.add(depositDTO);
        depositDTOList.add(depositDTO1);
        depositDTOList.add(depositDTO2);

        depositQueue.addAll(depositDTOList);

        assertNotNull(depositQueue);
        assertEquals(depositDTO.getDepositID(), depositQueue.peek().getDepositID());
        assertEquals(3, depositQueue.size());

    }

    @Test
    public void testAddingEmptyListToQueue()
    {
        List<DepositDTO> emptyList = new ArrayList<>();

        depositQueue.addAll(emptyList);

        assertNotNull(depositQueue);
        assertEquals(0, depositQueue.size());
    }

    @Test
    public void testAddingNullElementToQueue()
    {
        assertThrows(NullPointerException.class, () -> {
            depositQueue.add(null);
        });

        assertThrows(NullPointerException.class, () -> {
            depositQueue.addAll(null);
        });
    }

    @Test
    public void testOrderOfDepositsInQueue()
    {
        List<DepositDTO> depositDTOList = Arrays.asList(depositDTO, depositDTO1, depositDTO2);

        depositQueue.addAll(depositDTOList);

        assertEquals(depositDTO, depositQueue.poll());
        assertEquals(depositDTO1, depositQueue.poll());
        assertEquals(depositDTO2, depositQueue.poll());
    }


    @Test
    public void testRemovingDepositFromQueue()
    {
        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO1);

        DepositDTO removed = depositQueue.remove();

        assertEquals(depositDTO, removed);
        assertEquals(1, depositQueue.size());
        assertEquals(depositDTO1, depositQueue.peek());
    }

    @Test
    public void testCheckingDuplicateElementInQueue()
    {
        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO);

        boolean isDuplicate = depositQueue.isDuplicate(depositDTO);

        assertTrue(isDuplicate);
    }

    @Test
    public void testCheckingForMultipleDuplicateElementsInQueue()
    {
        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO1);
        depositQueue.add(depositDTO1);
        depositQueue.add(depositDTO2);

        boolean isDuplicate = depositQueue.isDuplicate(depositDTO);
        boolean isNextDuplicate = depositQueue.isDuplicate(depositDTO1);
        boolean isNotDuplicate = depositQueue.isDuplicate(depositDTO2);

        assertTrue(isDuplicate);
        assertTrue(isNextDuplicate);
        assertFalse(isNotDuplicate);
    }

    @Test
    public void testAddingDuplicateDepositsToQueue()
    {
        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO);

        assertTrue(depositQueue.isDuplicate(depositDTO));
        assertEquals(2, depositQueue.size());
    }

    @Test
    public void testAddQueueToDatabase()
    {
        DepositDTO dto = DepositDTO.builder()
                        .depositID(1)
                        .accountCode("A1")
                        .userID(1)
                        .description("Transfer1")
                        .amount(new BigDecimal("1214"))
                        .scheduleInterval(ScheduleType.ONCE)
                        .date(LocalDate.now())
                        .timeScheduled(LocalTime.now())
                        .build();
        depositQueue.addToDatabase(dto);

        DepositsEntity depositQueueEntity = convertToDepositEntity(depositDTO);
        DepositQueueEntity depositQueueEntity1 = convertToQueueEntity(depositQueueEntity);


        verify(queueService).save(any(DepositQueueEntity.class));
    }

    private DepositsEntity convertToDepositEntity(DepositDTO depositDTO)
    {
        return DepositsEntity.builder()
                .depositID(depositDTO.getDepositID())
                .amount(depositDTO.getAmount())
                .description(depositDTO.getDescription())
                .scheduledDate(depositDTO.getDate())
                .scheduledTime(depositDTO.getTimeScheduled())
                .posted(depositDTO.getDate())
                .scheduleInterval(depositDTO.getScheduleInterval())
                .build();
    }

    private DepositQueueEntity convertToQueueEntity(DepositsEntity deposits)
    {
        return DepositQueueEntity.builder()
                .deposit(deposits)
                .queuedAt(Timestamp.from(Instant.now()))
                .status(QueueStatus.PENDING)
                .build();
    }

    @AfterEach
    void tearDown() {
    }
}