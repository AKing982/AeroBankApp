package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.entity.InvestmentAccountEntity;
import com.example.aerobankapp.entity.RentAccountEntity;
import com.example.aerobankapp.entity.SavingsAccountEntity;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import com.example.aerobankapp.workbench.utilities.UserProfile;
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
   private Set<CheckingAccountEntity> allowedCheckingAccounts;
   private Set<SavingsAccountEntity> allowedSavingsAccounts;
   private Set<InvestmentAccountEntity> allowedInvestmentAccounts;
   private Set<RentAccountEntity> allowedRentAccounts;
   private Set<CardDesignator> userPaymentCards;
   private UserProfile currentUserProfile;

   @Autowired
   public UserSecurityModelImpl(@Qualifier("beanString") String username)
   {
      usernameCheck(username);
      this.currentUserProfile = new UserProfile(username);
   }

   private void usernameCheck(String username)
   {
      if(username == null || username.trim().isEmpty())
      {
         throw new IllegalArgumentException("Username cannot be null or Empty");
      }
   }

   @Override
   public Set<CheckingAccountEntity> getCheckingAccountDetails() {

      List<CheckingAccountEntity> checkingAccountEntityList = getCurrentUserProfile().getCheckingAccounts();
      return new HashSet<>(checkingAccountEntityList);
   }

   @Override
   public Set<SavingsAccountEntity> getSavingsAccountDetails() {
      return null;
   }

   @Override
   public Set<InvestmentAccountEntity> getInvestmentAccountDetails() {
      return null;
   }

   @Override
   public Set<RentAccountEntity> getRentAccountDetails() {
      return null;
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
