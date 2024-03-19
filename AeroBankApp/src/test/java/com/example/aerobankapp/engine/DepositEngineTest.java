package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.dto.ProcessedDepositDTO;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.DepositQueueService;
import com.example.aerobankapp.services.DepositService;
import com.example.aerobankapp.services.NotificationService;
import com.example.aerobankapp.workbench.transactions.Deposit;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositEngineTest {



    @BeforeEach
    void setUp() {


    }

    @AfterEach
    void tearDown() {
    }
}