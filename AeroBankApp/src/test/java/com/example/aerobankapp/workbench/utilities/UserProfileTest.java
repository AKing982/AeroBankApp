package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.account.AccountNumber;
import com.example.aerobankapp.account.AccountPrefix;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.CheckingAccountModel;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.services.UserDAOImpl;
import com.example.aerobankapp.services.UserProfileService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class UserProfileTest {

    @MockBean
    private UserProfile userProfile;

    private String mockUser;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testConstructorForNull() {
        assertThrows(NullPointerException.class,
                () -> userProfile = new UserProfile(null));
    }

    @Test
    public void testConstructorWithValidUserName() {
        User mockUser = User.builder()
                .userID(1)
                .userName("AKing")
                .password("Halflifer45!")
                .role(Role.ADMIN)
                .build();

        userProfile = new UserProfile(mockUser);

        assertEquals(mockUser, userProfile.getUsername());
    }


    @Test
    public void testDistinctUserProfilesWithDistinctUserNames()
    {
        User bsmith = User.builder()
                .userID(2)
                .userName("BSmith23")
                .password("HGamer21")
                .role(Role.CUSTOMER)
                .build();

        User aking = User.builder()
                .userID(1)
                .userName("AKing")
                .password("Halflifer45!")
                .role(Role.ADMIN)
                .build();

        UserProfile akingProfile = new UserProfile(aking);

        UserProfile bsmithProfile = new UserProfile(bsmith);

        assertNotNull(akingProfile);
        assertNotNull(bsmithProfile);
        assertEquals(aking.getUserName(), akingProfile.getUsername());
        assertEquals(bsmith.getUserName(), bsmithProfile.getUsername());
        assertNotEquals(akingProfile.getUsername(), bsmithProfile.getUsername());
        assertNotEquals(akingProfile, bsmithProfile);
        assertNotEquals(akingProfile.getAllAccounts(), bsmithProfile.getAllAccounts());
    }


    @AfterEach
    void tearDown()
    {
        mockUser = "";
        userProfile = null;
    }
}