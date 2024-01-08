package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.entity.CheckingAccountEntity;
import com.example.aerobankapp.entity.InvestmentAccountEntity;
import com.example.aerobankapp.entity.RentAccountEntity;
import com.example.aerobankapp.entity.SavingsAccountEntity;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class UserSecurityModelImpl implements UserSecurityModel
{
   private boolean isEnabled;
   private Set<CheckingAccountEntity> allowedCheckingAccounts;
   private Set<SavingsAccountEntity> allowedSavingsAccounts;
   private Set<InvestmentAccountEntity> allowedInvestmentAccounts;
   private Set<RentAccountEntity> allowedRentAccounts;
   private Set<CardDesignator> userPaymentCards;
   private UserProfile currentUserProfile;
   private final SecurityUser securityUser;

   @Autowired
   public UserSecurityModelImpl(SecurityUser securityUser)
   {
      this.securityUser = securityUser;
   }

   @Override
   public Set<CheckingAccountEntity> getCheckingAccountDetails() {
      return null;
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
   public boolean isUserEnabled() {
      return false;
   }
}
