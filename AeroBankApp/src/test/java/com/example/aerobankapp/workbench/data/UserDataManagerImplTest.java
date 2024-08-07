package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import com.example.aerobankapp.exceptions.InvalidUserDataException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.AccountNumber;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.generator.AccountNumberGenerator;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.dbUtils.DatabaseUtilities;
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
    private AccountNotificationService accountNotificationService;

    @Autowired
    private AccountCodeService accountCodeService;

    @Autowired
    private AccountUsersEntityService accountUsersEntityService;

    @Autowired
    private PlaidLinkService plaidAccountsService;

    @Autowired
    private UserLogService userLogService;

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

    @Autowired
    private DatabaseUtilities databaseUtilities;

    private int INVALID_USERID = -1;


    @BeforeEach
    void setUp() {
        userDataManager = new UserDataManagerImpl(userService, accountService, accountSecurityService, accountPropertiesService,accountNotificationService,plaidAccountsService, accountCodeService,  accountUsersEntityService, userLogService, accountNumberGenerator, databaseUtilities);
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
       // when(accountNumberGenerator.generateAccountNumber(username)).thenReturn(expectedAccountNumber);

        // Act
        AccountNumber result = userDataManager.buildAccountNumber(username);
        String actualStr = result.getAccountNumberToString();
        System.out.println("Actual: " + actualStr);
        String expectedStr = expectedAccountNumber.getAccountNumberToString();
        System.out.println("Expected: " + expectedStr);

        // Assert
        assertNotNull(result, "Account number should not be null");
        assertEquals(expectedStr, actualStr, "Generated account number should match expected");
   //     verify(accountNumberGenerator).generateAccountNumber(username); // Ensure our generator is called correctly
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

    @Test
    public void testGetUserLogsByUserID_InvalidUserID(){
        assertThrows(InvalidUserIDException.class, () -> {
            userDataManager.getUserLogsByUserID(INVALID_USERID);
        });
    }

    @Test
    public void testGetUserLogsByUserID_ValidUserID(){
        final int userID = 1;

        List<UserLogEntity> userLogEntities = userDataManager.getUserLogsByUserID(userID);

        assertNotNull(userLogEntities);
        assertFalse(userLogEntities.isEmpty());
        assertEquals(1, userLogEntities.size());
    }

    @Test
    public void testBuildUserEntity_NullUserOrNullAccountNumber(){
        assertThrows(InvalidUserDataException.class, () -> {
            userDataManager.buildUserEntity(null, null);
        });

        assertThrows(InvalidUserDataException.class, () -> {
            userDataManager.buildUserEntity(null, new AccountNumber(22, 22, 22));
        });
    }

    @Test
    public void testBuildUserEntity_ValidUser_ValidAccountNumber(){
        User user = User.builder()
                .username("AKing94")
                .email("alex@utahkings.com")
                .firstName("Alex")
                .lastName("King")
                .password("Halflifer45!")
                .pinNumber("5988")
                .role(Role.ADMIN)
                .build();

        AccountNumber accountNumber = new AccountNumber(22, 42, 24);
        UserEntity actualUserEntity = userDataManager.buildUserEntity(user, accountNumber);

        assertNotNull(actualUserEntity);
        assertEquals(user.getUsername(), actualUserEntity.getUserCredentials().getUsername());
        assertEquals(user.getFirstName(), actualUserEntity.getUserDetails().getFirstName());
        assertEquals(accountNumber.getAccountNumberToString(), actualUserEntity.getUserDetails().getAccountNumber());
    }

    @Test
    public void testCreateUser_NullUser(){
        assertThrows(InvalidUserDataException.class, () -> {
            userDataManager.createUser(null);
        });
    }

    @Test
    public void testCreateUser_ValidUser(){
        User user = User.builder()
                .username("BAdams223")
                .email("badams@outlook.com")
                .firstName("Ben")
                .lastName("Adams")
                .password("password")
                .pinNumber("2222")
                .role(Role.ADMIN)
                .build();

        boolean isCreated = userDataManager.createUser(user);

        assertTrue(isCreated);
    }

    @Test
    public void testCreateUser_UserAlreadyExists(){
        User user = User.builder()
                .username("AKing94")
                .email("alex@utahkings.com")
                .firstName("Alex")
                .lastName("King")
                .password("Halflifer45!")
                .pinNumber("2222")
                .role(Role.ADMIN)
                .build();

        boolean isCreated = userDataManager.createUser(user);

        assertFalse(isCreated);
    }

    @Test
    public void testModifyUser_NullUser(){
        assertThrows(InvalidUserDataException.class, () -> {
            userDataManager.modifyUser(null);
        });
    }

    @Test
    public void testModifyingUser_ValidUser(){
        AccountNumber accountNumber = new AccountNumber(89, 42, 48);
        User user = User.builder()
                .username("AKing94")
                .userID(1)
                .email("alex@utahkings.com")
                .firstName("Alex")
                .lastName("King")
                .password("Halflifer45!")
                .pinNumber("5988")
                .accountNumber(accountNumber)
                .role(Role.ADMIN)
                .build();

        boolean isModified = userDataManager.modifyUser(user);
        assertTrue(isModified);
    }

    @Test
    public void testCascadeDeleteAllUserData_InvalidUserID(){
        assertThrows(InvalidUserIDException.class, () -> {
            userDataManager.cascadeDeleteAllUserData(INVALID_USERID);
        });
    }

    @Test
    public void testCascadeDeleteAllUserData_ValidUserID(){
        boolean isDeleted = userDataManager.cascadeDeleteAllUserData(3);

        assertTrue(isDeleted);
    }



    @AfterEach
    void tearDown() {
    }
}