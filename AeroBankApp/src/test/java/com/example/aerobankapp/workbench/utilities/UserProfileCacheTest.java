package com.example.aerobankapp.workbench.utilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.HashMap;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class UserProfileCacheTest
{
    private UserProfileCache userProfileCache;

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