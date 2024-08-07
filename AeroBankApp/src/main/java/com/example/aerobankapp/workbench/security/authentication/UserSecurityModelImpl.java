package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.workbench.utilities.User;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import com.example.aerobankapp.workbench.utilities.UserProfileFacade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class UserSecurityModelImpl implements UserSecurityModel
{
   private boolean isEnabled;
   private Set<AccountEntity> allowedCheckingAccounts;
   private UserProfile currentUserProfile;



   private UserProfileFacade userProfileFacade;


   public UserSecurityModelImpl(User user)
   {
     // usernameCheck(username);
      this.currentUserProfile = new UserProfile(user);
   }

   private void usernameCheck(String username)
   {
      if(username == null || username.trim().isEmpty())
      {
         throw new IllegalArgumentException("Username cannot be null or Empty");
      }
   }

   @Override
   public Set<AccountEntity> getAccountDetails() {

      return new HashSet<>();
   }


   @Override
   public boolean isUserEnabled()
   {
      return false;
   }
}