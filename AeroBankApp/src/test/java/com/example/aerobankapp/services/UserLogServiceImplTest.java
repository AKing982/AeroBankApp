package com.example.aerobankapp.services;

import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.repositories.UserLogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserLogServiceImplTest {

    @InjectMocks
    private UserLogServiceImpl userLogService;

    @Autowired
    private UserLogRepository userLogRepository;

    @BeforeEach
    void setUp() {
        userLogService = new UserLogServiceImpl(userLogRepository);
    }

    @Test
    public void testFindByUserName_EmptyUserString(){
        String emptyUserStr = "";

        UserLogEntity userLogEntity = buildUserLogEntity(1, 1, "AKing94", 5000, 1, true, true, LocalDateTime.now(), LocalDateTime.now());
        List<UserLogEntity> userLogEntities = List.of(userLogEntity);

        assertThrows(IllegalArgumentException.class, () -> {
            List<UserLogEntity> foundUserLog = userLogService.findByUserName(emptyUserStr);
        });
    }

    @Test
    public void testFindByUserName_ValidUser(){
        String validUser = "AKing94";
        UserLogEntity userLogEntity = buildUserLogEntity(1, 1, "AKing94", 5000, 1, true, true, LocalDateTime.now(), LocalDateTime.now());
        List<UserLogEntity> userLogEntities = List.of(userLogEntity);

        userLogService.save(userLogEntity);
        List<UserLogEntity> foundUserLogs = userLogService.findByUserName(validUser);

        assertEquals(userLogEntities.size(), foundUserLogs.size());
        assertEquals(userLogEntities.get(0).getId(), foundUserLogs.get(0).getId());
    }

    @Test
    public void testGetUserLogByNumberOfLoginAttempts_InvalidUserID_InvalidAttempts(){
        final int invalidAttempts = -1;
        final int invalidUserID = -1;
        UserLogEntity userLogEntity = buildUserLogEntity(1, 1, "AKing94", 5000, 1, true, true, LocalDateTime.now(), LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> {
            UserLogEntity foundUserLog = userLogService.getUserLogByNumberOfLoginAttempts(invalidAttempts, invalidUserID);
        });
    }

    @Test
    public void testGetUserLogByNumberOfLoginAttempts_ValidUser(){
        final int attempts = 1;
        final int userID = 1;
        UserLogEntity userLogEntity = buildUserLogEntity(1, 1, "AKing94", 5000, 1, true, true, LocalDateTime.now(), LocalDateTime.now());

        UserLogEntity foundUserLog = userLogService.getUserLogByNumberOfLoginAttempts(attempts, userID);

        assertNotNull(foundUserLog);
        assertNotEquals(userLogEntity, foundUserLog);
        assertEquals(userLogEntity.getId(), foundUserLog.getId());
    }

    @Test
    public void testGetCurrentLoggedOnUserID(){
        final Long id = 1L;
        final int userID = 1;

        int actualFoundUserID = userLogService.getCurrentLoggedOnUserID(id);

        assertEquals(userID, actualFoundUserID);
    }

    @Test
    public void testFindActiveUserLogSessionByUserID_InvalidUserID(){
        final int userID = -1;
        UserLogEntity userLogEntity = buildUserLogEntity(1, 1, "AKing94", 5000, 1, true, true, LocalDateTime.now(), LocalDateTime.now());
        Optional<UserLogEntity> expectedOptionalUser = Optional.of(userLogEntity);

        assertThrows(InvalidUserIDException.class, () -> {
            Optional<UserLogEntity> optionalUserLogEntity = userLogService.findActiveUserLogSessionByUserID(userID);
        });
    }

    @Test
    public void testFindActiveUserLogSessionByUserID_ValidUserID(){
        final int userID = 1;
        UserLogEntity userLogEntity = buildUserLogEntity(15, 15, "AKing94", 5000, 1, true, true, LocalDateTime.now(), LocalDateTime.now());
        Optional<UserLogEntity> expectedOptionalUser = Optional.of(userLogEntity);

        Optional<UserLogEntity> actualUserLog = userLogService.findActiveUserLogSessionByUserID(userID);

        assertNotNull(actualUserLog);
        assertEquals(15, actualUserLog.get().getId());
    }

    @Test
    public void testFindActiveUserLogSessionByUserID_ValidNonExistingUser(){
        final int userID = 2;
        UserLogEntity userLogEntity = buildUserLogEntity(15, 15, "AKing94", 5000, 1, true, true, LocalDateTime.now(), LocalDateTime.now());
        Optional<UserLogEntity> expectedOptionalUser = Optional.of(userLogEntity);

        assertThrows(EntityNotFoundException.class, () -> {
            userLogService.findActiveUserLogSessionByUserID(userID);
        });
    }


    private UserLogEntity buildUserLogEntity(int id, int userID, String user,
                                             int sessionDuration,
                                             int loginAttempts,
                                             boolean loginSuccess,
                                             boolean isActive,

                                             LocalDateTime lastLogin,
                                             LocalDateTime lastLogout){

        // Create and set UserDetails
        UserDetails userDetails = UserDetails.builder()
                .firstName("Alex")  // Assuming default values, adjust as necessary
                .lastName("King")   // Assuming default values, adjust as necessary
                .email("alex@utahkings.com")  // Assuming default values, adjust as necessary
                .build();

        // Create and set LoginCredentials
        UserCredentials loginCredentials = UserCredentials.builder()
                .username(user)
                .build();

        // Create and set UserSecurity
        UserSecurity userSecurity = UserSecurity.builder()
                .isEnabled(isActive)
                .build();

        // Create and set UserEntity
        UserEntity userEntity = UserEntity.builder()
                .userID(userID)
                .userDetails(userDetails)
                .userCredentials(loginCredentials)
                .userSecurity(userSecurity)
                .build();

        // Create and set UserLogEntity
        UserLogEntity userLogEntity = new UserLogEntity();
        userLogEntity.setId(id);
        userLogEntity.setUserEntity(userEntity);
        userLogEntity.setSessionDuration(sessionDuration);
        userLogEntity.setLastLogin(String.valueOf(lastLogin));
        userLogEntity.setLoginAttempts(loginAttempts);
        userLogEntity.setLastLogout(String.valueOf(lastLogout));
        userLogEntity.setLoginSuccess(loginSuccess);
        userLogEntity.setActive(isActive);
        return userLogEntity;
    }

    @AfterEach
    void tearDown()
    {

    }
}