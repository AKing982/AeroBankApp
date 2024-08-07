package com.example.aerobankapp.workbench.utilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

        assertEquals(mockUser, userProfile.getUserName());
    }


    @Test
    public void testDistinctUserProfilesWithDistinctUserNames()
    {
        User bsmith = User.builder()
                .userID(2)
                .userName("BSmith23")
                .password("HGamer21")
                .role(Role.USER)
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
        assertEquals(aking.getUserName(), akingProfile.getUserName());
        assertEquals(bsmith.getUserName(), bsmithProfile.getUserName());
        assertNotEquals(akingProfile.getUserName(), bsmithProfile.getUserName());
        assertNotEquals(akingProfile, bsmithProfile);
     //   assertNotEquals(akingProfile.getAllAccounts(), bsmithProfile.getAllAccounts());
    }


    @AfterEach
    void tearDown()
    {
        mockUser = "";
        userProfile = null;
    }
}