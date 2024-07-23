package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGeneratorImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({JpaConfig.class, AppConfig.class})
class PlaidDataImporterImplTest {

    @InjectMocks
    private PlaidDataImporterImpl plaidAccountToSystemAccountMapper;


    @BeforeEach
    void setUp() {
        plaidAccountToSystemAccountMapper = new PlaidDataImporterImpl(accountService, accountCodeService, userDataManager, externalAccountsService);
    }




   private PlaidTransaction createPlaidTransaction(){
       PlaidTransaction transaction = new PlaidTransaction();
       transaction.setTransactionName("Test Transaction");
       transaction.setAccountId("e12123123123");
       transaction.setTransactionId("234234234234");
       transaction.setDate(LocalDate.of(2024, 6, 14));
       transaction.setAmount(BigDecimal.valueOf(120));
       transaction.setPending(false);
       return transaction;
   }










    @AfterEach
    void tearDown() {
    }
}