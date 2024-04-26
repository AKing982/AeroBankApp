package com.example.aerobankapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class AccountCodeCreatorImplTest {

    @InjectMocks
    private AccountCodeCreatorImpl accountCodeCreator;

    @Mock
    private AccountService accountService;

    @Mock
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

    }

    @AfterEach
    void tearDown() {
    }
}