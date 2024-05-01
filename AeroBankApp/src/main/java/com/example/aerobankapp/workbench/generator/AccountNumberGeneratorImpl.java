package com.example.aerobankapp.workbench.generator;

import com.example.aerobankapp.exceptions.AccountGenerationException;
import com.example.aerobankapp.exceptions.InvalidAccountSegmentException;
import com.example.aerobankapp.exceptions.InvalidUserStringException;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class AccountNumberGeneratorImpl implements AccountNumberGenerator
{
    private Logger aeroLogger = LoggerFactory.getLogger(AccountNumberGeneratorImpl.class);

    private UserRepository userService;

    @Autowired
    public AccountNumberGeneratorImpl(UserRepository userRepository){
        this.userService = userRepository;
    }

    public boolean validateGeneratedAccountNumber(final AccountNumber accountNumber){
        if(!accountNumber.isValid()){
            // If the account number doesn't exist in the database, then return true
            aeroLogger.info("Invalid Account Number: " + accountNumber);
            aeroLogger.info("Invalid Account Number as string: " + accountNumber.getAccountNumberToString());
            return false;
        }

        // Convert account number to string as it will be stored or checked in the database.
        String accountNumberAsStr = accountNumber.getAccountNumberToString();

        // Check if the account number already exists in the database.
        boolean exists = userService.doesAccountNumberExist(accountNumberAsStr);
        aeroLogger.info("Account Number Uniqueness Check: {}", exists);
        // The account number is valid only if it does NOT exist in the database (ensuring uniqueness).
        return !exists;
    }

    public boolean validateSegmentsParts(int part){
        return String.valueOf(part).length() == 2;
    }

    @Override
    public AccountNumber generateAccountNumber(String user) {
        if(user.isEmpty()){
            throw new InvalidUserStringException("Invalid user name found. Unable to proceed with creating account number.");
        }
        try {
            // Use SHA-256 hashing
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(user.getBytes());
            byte[] digest = md.digest();

            // Convert the hash bytes to integers and format
            int part1 = Math.abs(new BigInteger(1, new byte[]{digest[0], digest[1]}).intValue() % 100);
            int part2 = Math.abs(new BigInteger(1, new byte[]{digest[2], digest[3]}).intValue() % 100);
            int part3 = Math.abs(new BigInteger(1, new byte[]{digest[4], digest[5]}).intValue() % 100);

            aeroLogger.info("segment 1: " + part1);
            aeroLogger.info("segment 2: " + part2);
            aeroLogger.info("segment 3: " + part3);

            // Create the Account Number
            if (validateSegmentsParts(part1) && validateSegmentsParts(part2) && validateSegmentsParts(part3)) {
                AccountNumber accountNumber = new AccountNumber(part1, part2, part3);
                if(validateGeneratedAccountNumber(accountNumber)){
                    return accountNumber;
                }
            }else{
                throw new InvalidAccountSegmentException("Found invalid account segment.");
            }
            // use the validation method to verify account number doesn't already exist in the database.
            // If the Account Number is not valid, simply return null
            throw new AccountGenerationException("Generated AccountNumber is invalid or already exists.");

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating account number", e);
        }
    }
}
