package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.model.UserLogModel;
import com.example.aerobankapp.repositories.UserLogRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserLogServiceImplTest {

    @MockBean
    private UserLogServiceImpl userLogService;

    @Autowired
    private UserLogRepository userLogRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        userLogService = new UserLogServiceImpl(userLogRepository, entityManager);
    }

    @Test
    public void saveUserLog()
    {
        UserLog userLog = UserLog.builder()
                .userID(1)
                .username("AKing94")
                .lastLogin(new Date())
                .build();

        userLogService.save(userLog);
        List<UserLog> expected = Arrays.asList(userLog);
        List<UserLog> actual = userLogService.findByUserName("AKing94");

        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll()
    {
        List<UserLogModel> userLogModelList = Arrays.asList(new UserLogModel(), new UserLogModel());
        userLogService = mock(UserLogServiceImpl.class);

    }

    @Test
    public void testFindAllWithConstructor()
    {
     //   UserLogServiceImpl userLogService1 = new UserLogServiceImpl();

        List<UserLogModel> userLogModelList = Arrays.asList(new UserLogModel());
       // List<UserLogModel> actualList = userLogService1.findAll();

       // assertNotEquals(userLogModelList,actualList);
    }

    @AfterEach
    void tearDown() {
    }
}