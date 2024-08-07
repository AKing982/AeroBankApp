package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.converter.BillPaymentConverter;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.workbench.data.BillPaymentDataManager;
import com.example.aerobankapp.workbench.processor.BillPaymentProcessor;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BillPaymentRunnerTest {

    @InjectMocks
    private BillPaymentRunner billPaymentRunner;


    @BeforeEach
    void setUp() {

    }

    static BillPaymentEntity createBillPaymentEntity() {
        // replace with actual BillPaymentEntity creation
        return BillPaymentEntity.builder()
                .paymentAmount(new BigDecimal("100.00"))
                .paymentType("ACCOUNT")
                .paymentID(1L)
                .user(UserEntity.builder().userID(1).build())
                .payeeName("Payee Test")
                .account(AccountEntity.builder().acctID(1).build())
                .postedDate(LocalDate.now())
                .isProcessed(false)
                .paymentSchedule(null) // You may need to adjust this according to your test cases
                .build();
    }

    static BillPaymentScheduleEntity createBillPaymentScheduleEntity() {
        // replace with actual BillPaymentScheduleEntity creation
        return BillPaymentScheduleEntity.builder()
                .paymentScheduleID(1L)
                .scheduledPaymentDate(LocalDate.of(2024, 5, 28))
                .paymentDueDate(LocalDate.of(2024, 6, 1))
                .scheduleFrequency(ScheduleFrequency.MONTHLY)
                .scheduleStatus(ScheduleStatus.PENDING)
                .isRecurring(true)
                .autoPayEnabled(true)
                .billPayments(new HashSet<>()) // You may need to adjust this according to your test cases
                .billPaymentHistory(null)  // You may need to adjust this according to your test cases
                .build();
    }

    @AfterEach
    void tearDown() {
    }
}