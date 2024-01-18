package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.repositories.UserLogRepository;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.services.UserDAOImpl;
import com.example.aerobankapp.services.UserLogDAOImpl;
import com.example.aerobankapp.workbench.model.LoginModel;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserLogRunnerTest {
    @MockBean
    private UserLogRunner runner;

    @Autowired
    private UserLogDAOImpl userLogService;

    @Autowired
    private UserDAOImpl userService;

    @Autowired
    private UserLogRepository userLogRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LoginModel loginModel;

    @BeforeEach
    void setUp() {
        String user = "AKing94";
        String pass = "Halflifer94!";
        loginModel = new LoginModel(user, pass);

        runner = new UserLogRunner(userLogService, userService, loginModel);
        userLogService = new UserLogDAOImpl(userLogRepository, entityManager);
    }

    @Test
    public void testUserIDRetrieval() {
        UserEntity user = UserEntity.builder()
                .email("alex@utahkings.com")
                .password("Halflifer45!")
                .username("AKing94")
                .pinNumber("5988")
                .isAdmin(true)
                .isEnabled(true)
                .build();

        int expectedID = 1;
        int actualID = runner.getCurrentUserID();

        assertNotNull(runner);
        assertEquals(expectedID, actualID);
    }

    @Test
    public void testAddingUserLog() {
        UserLogEntity userLog = UserLogEntity.builder()
                .userID(1)
                //.lastLogin(new Date())
                .username("AKing94")
                .build();

        runner.storeUserLog(userLog);

        List<UserLogEntity> userLog1 = userLogService.findByUserName("AKing94");

        assertEquals(userLog, userLog1);
    }

    @Test
    public void testAddingNullUserLog() {
        UserLogEntity userLog = null;
        runner.storeUserLog(userLog);

        List<UserLogEntity> userLogList = userLogService.findByUserName("AKing94");

        assertNull(userLogList);
    }

    @Test
    public void createUserLog() {
        String username = "AKing94";
        int userID = 1;
        Date date = new Date();

        UserLogEntity userLog = UserLogEntity.builder()
                .username(username)
                .userID(userID)
               // .lastLogin(date)
                .build();

       // UserLogEntity actual = runner.createUserLog(username, userID, date);

    //    assertNotNull(actual);
      //  assertEquals(userLog, actual);
    }

    @Test
    public void addUserLogToDatabase()
    {
        String username = "AKing94";
        int userID = 1;
        Date date = new Date();

        UserLogEntity userLog = UserLogEntity.builder()
                .username(username)
                .userID(userID)
               // .lastLogin(date)
                .build();

        runner.addUserLogToDatabase();

        List<UserLogEntity> actual = userLogService.findByUserName(username);
        assertNotEquals(userLog, actual.get(0));
    }

    @Test
    public void testRunner()
    {

    }

    @AfterEach
    void tearDown() {
    }
}