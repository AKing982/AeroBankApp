package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.repositories.UserLogRepository;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.services.UserLogServiceImpl;
import com.example.aerobankapp.services.UserServiceImpl;
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
    private UserLogServiceImpl userLogService;

    @Autowired
    private UserServiceImpl userService;

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
        userLogService = new UserLogServiceImpl(userLogRepository, entityManager);
    }

    @Test
    public void testUserIDRetrieval() {
        Users user = Users.builder()
                .email("alex@utahkings.com")
                .password("Halflifer45!")
                .username("AKing94")
                .pinNumber("5988")
                .isAccountNonExpired(true)
                .isAdmin(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();

        int expectedID = 1;
        int actualID = runner.getCurrentUserID();

        assertNotNull(runner);
        assertEquals(expectedID, actualID);
    }

    @Test
    public void testAddingUserLog() {
        UserLog userLog = UserLog.builder()
                .userID(1)
                .lastLogin(new Date())
                .username("AKing94")
                .build();

        runner.storeUserLog(userLog);

        List<UserLog> userLog1 = userLogService.findByUserName("AKing94");

        assertEquals(userLog, userLog1);
    }

    @Test
    public void testAddingNullUserLog() {
        UserLog userLog = null;
        runner.storeUserLog(userLog);

        List<UserLog> userLogList = userLogService.findByUserName("AKing94");

        assertNull(userLogList);
    }

    @Test
    public void createUserLog() {
        String username = "AKing94";
        int userID = 1;
        Date date = new Date();

        UserLog userLog = UserLog.builder()
                .username(username)
                .userID(userID)
                .lastLogin(date)
                .build();

        UserLog actual = runner.createUserLog(username, userID, date);

        assertNotNull(actual);
        assertEquals(userLog, actual);
    }

    @Test
    public void addUserLogToDatabase()
    {
        String username = "AKing94";
        int userID = 1;
        Date date = new Date();

        UserLog userLog = UserLog.builder()
                .username(username)
                .userID(userID)
                .lastLogin(date)
                .build();

        runner.addUserLogToDatabase();

        List<UserLog> actual = userLogService.findByUserName(username);
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