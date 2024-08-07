package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.dto.AccountInfoDTO;

import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserDTO;
import com.example.aerobankapp.workbench.utilities.Role;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class AccountCodeCreatorImplTest {

    @InjectMocks
    private AccountCodeCreatorImpl accountCodeCreator;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        accountCodeCreator = new AccountCodeCreatorImpl(accountService, userService);
    }

    @Test
    public void testGetTwoDigitYearSegment_InvalidYear(){
        int year = -1;

        assertThrows(IllegalArgumentException.class, () -> {
            accountCodeCreator.getTwoDigitYearSegment(year);
        });
    }

    @Test
    public void testGetTwoYearSegment_returnValidYear(){
        int year = 2024;

        int expected = 24;
        int actual = accountCodeCreator.getTwoDigitYearSegment(year);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSequenceNumber(){
        int sequenceNumber = 6;
        int actual = accountCodeCreator.getSequenceNumber();

        assertEquals(sequenceNumber, actual);
    }

    @Test
    public void testGenerateAccountCode_NullUserDTO_NullAccountInfoDTO(){
        assertThrows(NullPointerException.class, () -> {
            accountCodeCreator.generateAccountCode(null, null);
        });
    }

    @Test
    public void testGenerateAccountCode_ValidParameters(){
        AccountNumber accountNumber = new AccountNumber(48, 22, 42);
        User userDTO = new User("Alex", "King", "AKing94", "alex@utahkings.com", "Halflifer45!", "5988", true, Role.ADMIN, accountNumber);
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO("Alex Checking", AccountType.CHECKING.name(), new BigDecimal("5678"));

        AccountCode accountCode = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 6);

        AccountCode actual = accountCodeCreator.generateAccountCode(userDTO, accountInfoDTO);
        System.out.println(actual.toString());

        assertNotNull(actual);
        assertEquals(accountCode.getFirstInitial(), actual.getFirstInitial());
        assertEquals(accountCode.getLastInitial(), actual.getLastInitial());
        assertEquals(accountCode.getAccountType(), actual.getAccountType());
        assertEquals(accountCode.getUserID(), actual.getUserID());
        assertEquals(accountCode.getYear(), actual.getYear());
        assertEquals(accountCode.getSequence(), actual.getSequence());
    }

    @Test
    public void testFormatAccountCode(){
        AccountCode accountCode = new AccountCode("A", "K", 1, AccountType.CHECKING, 24, 6);

        String accountCodeAsString = "AK01-0124006";

        String actualAccountCode = accountCodeCreator.formatAccountCode(accountCode);

        assertEquals(accountCodeAsString, actualAccountCode);
    }

    @AfterEach
    void tearDown() {
    }
}