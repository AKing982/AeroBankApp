package com.example.aerobankapp;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.DepositQueueService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositQueueTest {


    private DepositQueue depositQueue;

    @Autowired
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
        assertEquals(depositDTO.depositID(), depositQueue.peek().depositID());
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

        boolean isDuplicate = depositQueue.isDuplicate(depositDTO);
        boolean isNextDuplicate = depositQueue.isDuplicate(depositDTO1);

        assertTrue(isDuplicate);
        assertTrue(isNextDuplicate);
    }

    @Test
    public void testAddingDuplicateDepositsToQueue()
    {
        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO);

        assertEquals(2, depositQueue.size());
    }

    @Test
    public void testRemovingDuplicatesFromQueue()
    {
        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO);
        depositQueue.add(depositDTO1);
        depositQueue.add(depositDTO1);

        boolean isDuplicate = depositQueue.isDuplicate(depositDTO);
        boolean isNextDuplicate = depositQueue.isDuplicate(depositDTO1);
        DepositDTO removedDup1 = depositQueue.removeDuplicate(depositDTO);
        DepositDTO removedDup2 = depositQueue.removeDuplicate(depositDTO1);

        assertTrue(isDuplicate);
        assertTrue(isNextDuplicate);
        assertEquals(removedDup1, depositQueue.removeDuplicate(depositDTO));
        assertEquals(removedDup2, depositQueue.removeDuplicate(depositDTO1));
        assertEquals(2, depositQueue.size());
    }

    @AfterEach
    void tearDown() {
    }
}