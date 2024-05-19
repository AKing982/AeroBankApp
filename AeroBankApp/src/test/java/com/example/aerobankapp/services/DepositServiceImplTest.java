package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.DepositRepository;
import com.example.aerobankapp.scheduler.ScheduleType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@Transactional
class DepositServiceImplTest
{
    @Autowired
    private DepositServiceImpl depositService;

    @Autowired
    private EntityManager entityManager;

    @MockBean
    private DepositRepository depositRepository;

    @MockBean
    private AsyncDepositService asyncDepositService;


    @BeforeEach
    void setUp()
    {

        depositService = new DepositServiceImpl(depositRepository, entityManager);
    }

    @Test
    public void testGetDepositsByAcctID()
    {
        List<DepositsEntity> depositsEntityList = depositService.getDepositsByAcctID(1);

        assertNotNull(depositsEntityList);
        assertEquals(2, depositsEntityList.size());
    }

    @Test
    public void testFindByUserName_ValidUser()
    {
        // Arrange
        String user = "AKing94";
        DepositsEntity deposits = createMockDepositEntity(1, 1, "A1", 1, new BigDecimal("1200"), "Transfer 1", ScheduleType.ONCE, LocalDate.now(),LocalDate.of(2024, 2, 2), LocalTime.now());
        DepositsEntity deposits2 = createMockDepositEntity(2, 1, "A2", 2, new BigDecimal("45.00"), "Savings Transfer", ScheduleType.ONCE, LocalDate.now(),LocalDate.of(2024, 2, 5), LocalTime.now());
        List<DepositsEntity> mockDeposits = Arrays.asList(deposits, deposits2);

        // Act
        List<DepositsEntity> depositsEntityList = depositService.findByUserName(user);

        // Assert
        assertEquals(mockDeposits.get(0).getDepositID(), depositsEntityList.get(0).getDepositID());
        assertEquals(mockDeposits.get(0).getUser().getUserID(), depositsEntityList.get(0).getUser().getUserID());
        assertEquals(mockDeposits.size(), depositsEntityList.size());
    }

    @Test
    public void testFindByUserName_InvalidUser()
    {
        String user = null;

        assertThrows(IllegalArgumentException.class, () -> {
            List<DepositsEntity> depositsEntityList = depositService.findByUserName(user);
        });
    }

    @Test
    public void testGetDepositsByUserNameDesc_ValidUser_different_dates()
    {
        String user = "AKing94";
        DepositsEntity deposits = createMockDepositEntity(1, 1, "A1", 1, new BigDecimal("1200"), "Transfer 1", ScheduleType.ONCE, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 6), LocalTime.now());
        DepositsEntity deposits2 = createMockDepositEntity(2, 1, "A2", 2, new BigDecimal("45.00"), "Savings Transfer", ScheduleType.ONCE, LocalDate.of(2024, 2, 6), LocalDate.of(2024, 2, 5), LocalTime.now());
        List<DepositsEntity> mockDeposits = Arrays.asList(deposits, deposits2);

        // Act
        List<DepositsEntity> depositsEntities = depositService.getDepositsByUserNameDesc(user);

        assertEquals(mockDeposits.size(), depositsEntities.size());
        for (int i = 0; i < mockDeposits.size(); i++) {
            assertEquals(mockDeposits.get(i).getTransactionCriteria().getPosted(), depositsEntities.get(i).getTransactionCriteria().getPosted());
        }

        assertTrue(depositsEntities.get(0).getTransactionCriteria().getPosted().isAfter(depositsEntities.get(1).getTransactionCriteria().getPosted()));
    }

    private DepositsEntity createMockDepositEntity(int id, int userID, String acctCode, int acctID, BigDecimal amount, String description, ScheduleType scheduleType, LocalDate date, LocalDate posted, LocalTime scheduledTime)
    {
       DepositsEntity depositsEntity = new DepositsEntity();
       depositsEntity.setDepositID(id);
//       depositsEntity.setAmount(amount);
//       depositsEntity.setDescription(description);
       depositsEntity.setUser(UserEntity.builder().userID(userID).build());
//       depositsEntity.setScheduleInterval(scheduleType);
//       depositsEntity.setScheduledDate(date);
//       depositsEntity.setScheduledTime(scheduledTime);
//       depositsEntity.setPosted(posted);
    //   depositsEntity.setAccount(AccountEntity.builder().accountCode(acctCode).acctID(acctID).build());
       return depositsEntity;
    }




    @AfterEach
    void tearDown() {
    }
}