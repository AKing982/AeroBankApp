package com.example.aerobankapp;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.transactions.Deposit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Random;
import java.util.UUID;

public class DepositObjectCreator
{
    public static Deposit createRandomDeposit() {
        Random random = new Random();

        // Generate random values for each field
        long depositID = random.nextLong();
        int userID = 1; // example user ID
        String description = "Deposit - " + UUID.randomUUID().toString(); // random description
        String acctCode = "A1"; // example account code
        int accountID = 1; // example account ID
        BigDecimal amount = BigDecimal.valueOf(random.nextDouble() * 10000); // random amount
        LocalDateTime timeScheduled = LocalDateTime.now().plusDays(random.nextInt(30)); // random future time
        ScheduleType scheduleInterval = ScheduleType.values()[random.nextInt(ScheduleType.values().length)]; // random schedule type
        LocalDate datePosted = LocalDate.now().plusDays(random.nextInt(30)); // random future date
        Currency currency = Currency.getInstance("USD"); // assuming USD for simplicity

        // Create a new Deposit object with the random values
        return new Deposit(userID, description, acctCode, accountID, amount, timeScheduled, scheduleInterval, datePosted, currency, depositID);
    }

    // Example usage
    public static void main(String[] args) {
        Deposit randomDeposit = createRandomDeposit();
        System.out.println("Random Deposit: " + randomDeposit);
    }
}
