package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.services.UserProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserProfileCacheTest
{
    @MockBean
    private UserProfileCache userProfileCache;

    @Autowired
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp()
    {
        userProfileCache = new UserProfileCache();
    }

    @Test
    public void testAddingUserProfileToHash()
    {
        Hashtable<String, UserProfile> userProfileMap = new Hashtable<>();
        UserProfile userProfile = new UserProfile("AKing94");
        userProfileMap.put(userProfile.getUsername(), userProfile);

        userProfileCache.addUserProfileToCache(userProfile);
        UserProfile cachedProfile = userProfileCache.getCachedProfileByUser("AKing94");

        assertNotNull(cachedProfile);
        assertNotEquals(userProfileMap.get("AKing94"), cachedProfile);

    }

    @Test
    public void testAddingUserProfilesToHashTable()
    {
        UserProfile akingProfile = new UserProfile("AKing94");
        UserProfile bobProfile = new UserProfile("BSmith23");
        UserProfile samProfile = new UserProfile("SWilliam34");
        List<UserProfile> userProfiles = new ArrayList<>();
        userProfiles.add(akingProfile);
        userProfiles.add(bobProfile);
        userProfiles.add(samProfile);

        userProfileCache.saveProfiles(userProfiles);
        boolean isEmpty = userProfileCache.isCacheEmpty();
        boolean isSaved = userProfileCache.isSaved(akingProfile);
        int cacheSize = userProfileCache.size();

        assertFalse(isEmpty);
        assertTrue(isSaved);
        assertEquals(3, cacheSize);
    }

    @Test
    public void testGettingUserProfileFromCacheWhenCacheIsEmpty()
    {
        UserProfileCache userProfileCache1 = new UserProfileCache();

        UserProfile akingProfile = userProfileCache1.getCachedProfileByUser("AKing94");

        assertNotNull(akingProfile);
    }

    @Test
    public void testLoadCachedProfiles()
    {
        Hashtable<String, UserProfile> expectedCache = new Hashtable<>();
        Hashtable<String, UserProfile> cachedProfiles = userProfileCache.loadCachedProfiles();

        assertNotNull(cachedProfiles);
        assertEquals(expectedCache, cachedProfiles);
    }

    @AfterEach
    void tearDown() {
    }
}