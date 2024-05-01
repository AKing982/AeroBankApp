package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.generator.AccountNumberGenerator;
import com.example.aerobankapp.workbench.utilities.response.PasswordVerificationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserDataManagerImplTest {


    @InjectMocks
    private UserDataManagerImpl userDataManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountSecurityService accountSecurityService;

    @Autowired
    private AccountPropertiesService accountPropertiesService;

    @Autowired
    private AccountCodeService accountCodeService;

    @Autowired
    private AccountUsersEntityService accountUsersEntityService;

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;


    @BeforeEach
    void setUp() {
        userDataManager = new UserDataManagerImpl(userService, accountService, accountSecurityService, accountPropertiesService, accountCodeService, accountUsersEntityService, userLogService, accountNumberGenerator);
    }

    static Stream<Arguments> provideUsernamesAndAccountNumbers() {
        return Stream.of(
                Arguments.of("Alice", new AccountNumber(22,22,22)),
                Arguments.of("", new AccountNumber(78, 24, 25)),
                Arguments.of("Bob123", new AccountNumber(75, 69, 87)),
                Arguments.of("Charlie@#$$%", new AccountNumber(89, 22,23)),
                Arguments.of("BAdams332", new AccountNumber(80, 68, 22)),
                Arguments.of("MDowne445", new AccountNumber(57, 22, 37))
        );
    }

    @ParameterizedTest
    @MethodSource("provideUsernamesAndAccountNumbers")
    void testBuildAccountNumber(String username, AccountNumber expectedAccountNumber) {

        // Arrange
      //  when(accountNumberGenerator.generateAccountNumber(username)).thenReturn(expectedAccountNumber);

        // Act
        AccountNumber result = userDataManager.buildAccountNumber(username);
        String actualStr = result.getAccountNumberToString();
        String expectedStr = expectedAccountNumber.getAccountNumberToString();

        // Assert
        assertNotNull(result, "Account number should not be null");
        assertEquals(expectedStr, actualStr, "Generated account number should match expected");
      //  verify(accountNumberGenerator).generateAccountNumber(username); // Ensure our generator is called correctly
    }

    @Test
    public void testGetAccountsByUserID_InvalidUserID(){
        final int userID = -1;

        assertThrows(InvalidUserIDException.class, () -> {
            userDataManager.getAccountsByUserID(userID);
        });
    }

    @Test
    public void testGetAccountsByUserID_ValidUserID(){
        final int userID = 1;

        List<AccountEntity> accountEntities = userDataManager.getAccountsByUserID(userID);

        assertNotNull(accountEntities);
        assertFalse(accountEntities.isEmpty());
        assertEquals(3, accountEntities.size());
    }

    @Test
    public void testGetAccountsByUserID_ValidUserID_DNE_return_EmptyList(){
        final int userID = 4;

        List<AccountEntity> accountEntities = userDataManager.getAccountsByUserID(userID);
        assertNotNull(accountEntities);
        assertTrue(accountEntities.isEmpty());
        assertEquals(0, accountEntities.size());
    }

    @AfterEach
    void tearDown() {
    }
}