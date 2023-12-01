package com.example.aerobankapp.workbench.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Component
public class UserProfileCache
{
    private Hashtable<String, UserProfile> cachedProfiles = new Hashtable<>();

    public void addUserProfileToCache(UserProfile userProfile)
    {
        if(userProfile != null)
        {
            String user = userProfile.getUsername();
            cachedProfiles.put(user, userProfile);
        }
    }

    public UserProfile getCachedProfileByUser(String user)
    {
        UserProfile cachedProfile = null;
        if(cachedProfiles != null)
        {
            cachedProfile = cachedProfiles.get(user);
        }
        else
        {
            throw new NullPointerException();
        }

        return (UserProfile) cachedProfile.clone();
    }

    public Hashtable<String, UserProfile> loadCachedProfiles()
    {
        return cachedProfiles;
    }

    public void saveProfiles(List<UserProfile> userProfiles)
    {
        for(UserProfile userProfile : userProfiles)
        {
            addUserProfileToCache(userProfile);
        }
    }

    public boolean isCacheEmpty()
    {
        return cachedProfiles.isEmpty();
    }

    public int size()
    {
        return cachedProfiles.size();
    }

    public boolean isSaved(UserProfile userProfile)
    {
        return cachedProfiles.containsKey(userProfile.getUsername());
    }
}