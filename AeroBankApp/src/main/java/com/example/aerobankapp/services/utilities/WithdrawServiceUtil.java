package com.example.aerobankapp.services.utilities;

import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.workbench.utilities.Status;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public final class WithdrawServiceUtil
{
    public static void assertAccountIDInvalid(final int acctID){
        if(acctID < 1){
            throw new InvalidAccountIDException("Found Invalid AcctID: " + acctID);
        }
    }

    public static void assertDescriptionStringEmpty(final String description){
        if(description.isEmpty()){
            throw new IllegalArgumentException("Found empty description criteria.");
        }
    }

    public static void assertWithdrawEntityListNotEmpty(final Logger LOGGER, final List<WithdrawEntity> withdrawEntities){
        if(withdrawEntities.isEmpty()){
            LOGGER.warn("Expected to find withdrawals but found none.");
            throw new NoWithdrawEntitiesFoundException("No Withdraw Entities were found.");
        }
    }

    public static void assertUserIDNotInvalid(final int userID){
        if(userID < 1){
            throw new InvalidUserIDException("Caught Invalid UserID.");
        }
    }

    public static void assertStatusNotNull(final Status status){
        if(status == null){
            throw new StatusNotFoundException("Status not found. Unable to retrieve withdrawals.");
        }
    }

    public static void assertEmptyUserString(final Logger LOGGER, final String user){
        if(user.isEmpty()){
            LOGGER.warn("Expected non Empty user.");
            throw new InvalidUserCriteriaException("Invalid User Found: " + user + " unable to retrieve withdraws with invalid user.");
        }
    }

    public static void assertBeginAndEndDateNotNull(final LocalDate startDate, final LocalDate endDate){
        if(startDate == null || endDate == null){
            throw new IllegalArgumentException("Invalid Start Date or End Date Criteria found.");
        }
    }

    public static void assertWithdrawAmountNull(final Long id, final BigDecimal amount){
        if(amount == null){
            throw new WithdrawalAmountNotFoundException("No Withdrawal amount found for withdraw ID: " + id);
        }
    }

    public static void assertIDInvalid(final Long id){
        if(id < 1L){
            throw new InvalidUserIDException("Caught Invalid UserID");
        }
    }

    public static void assertOptionalWithdrawIsEmpty(Optional<WithdrawEntity> withdrawEntityOptional){
        if(withdrawEntityOptional.isEmpty()){
            throw new NoWithdrawEntitiesFoundException("No Withdraw Entities were found.");
        }
    }
}
