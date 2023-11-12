package com.example.aerobankapp.workbench.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Hashtable;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserProfileCache
{
    private Hashtable<String, UserProfile> cachedProfiles = new Hashtable<>();

    public void addUserProfileToCache(UserProfile userProfile)
    {
        cachedProfiles.put(userProfile.getUsername(), userProfile);
    }

    public UserProfile getCachedProfileByUser(String user)
    {
        UserProfile cachedProfile = cachedProfiles.get(user);
        return (UserProfile) cachedProfile.clone();
    }

    public Hashtable<String, UserProfile> loadCachedProfiles()
    {
        return cachedProfiles;
    }
}
