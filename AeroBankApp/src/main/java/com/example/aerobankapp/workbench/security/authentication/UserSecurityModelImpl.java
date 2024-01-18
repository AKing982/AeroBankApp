package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.services.UserProfileService;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import com.example.aerobankapp.workbench.utilities.User;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import com.example.aerobankapp.workbench.utilities.UserProfileFacade;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Builder
@AllArgsConstructor
@Getter
@Setter
public class UserSecurityModelImpl implements UserSecurityModel
{
   private boolean isEnabled;
   private Set<AccountEntity> allowedCheckingAccounts;
   private Set<CardDesignator> userPaymentCards;
   private UserProfile currentUserProfile;

   private UserProfileService userProfileService;

   @Autowired
   private UserProfileFacade userProfileFacade;

   @Autowired
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
   public Set<CardDesignator> getUserCardDetails() {
      return null;
   }

   @Override
   public boolean isUserEnabled()
   {
      return false;
   }
}
