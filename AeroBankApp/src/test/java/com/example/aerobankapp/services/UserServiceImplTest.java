package com.example.aerobankapp.services;

import com.example.aerobankapp.embeddables.UserCredentials;
import com.example.aerobankapp.embeddables.UserDetails;
import com.example.aerobankapp.embeddables.UserSecurity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidAccountNumberException;
import com.example.aerobankapp.exceptions.InvalidUserDTOException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserDTO;
import com.example.aerobankapp.repositories.UserRepository;

import com.example.aerobankapp.workbench.utilities.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private EntityManager manager;

    @Mock
    private TypedQuery<UserEntity> typedQuery;

    @BeforeEach
    void setUp()
    {


    }


    @Test
    public void testFindByUserName()
    {
        List<UserEntity> allUsers = userService.findByUserName("AKing94");

        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
    }


    @Test
    public void getAccountNumber_UserExists()
    {
        String expectedAccountNumber = "87-34-23";
        String username = "AKing94";
        String email = "alex@utahkings.com";
        String pin = "5988";
        String password = "Halflifer45!";
        Role role = Role.ADMIN;

        UserEntity mockUser = UserEntity.builder()
                .userDetails(UserDetails.builder().accountNumber(expectedAccountNumber).email(email).build())
                .userSecurity(UserSecurity.builder().pinNumber(pin).role(role).build())
                .userCredentials(UserCredentials.builder().username(username).password(password).build())
                .build();

        when(typedQuery.setParameter("user", "AKing94")).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(mockUser));

        String accountNumber = userService.getAccountNumberByUserName("AKing94");

        assertEquals(expectedAccountNumber, accountNumber);
    }

    @Test
    public void getAccountNumber_UserDoesNotExist()
    {
        when(typedQuery.setParameter("user", "Mike23")).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementException.class, () -> userService.getAccountNumberByUserName("Mike23"));
    }

    @Test
    public void getAccountNumber_InvalidUserName()
    {
        assertThrows(NoSuchElementException.class, () -> userService.getAccountNumberByUserName(null));
        assertThrows(NoSuchElementException.class, () -> userService.getAccountNumberByUserName(""));
    }

    @Test
    public void getListOfUserNames()
    {

        TypedQuery<String> mockQuery = mock(TypedQuery.class);
        when(mockQuery.getResultList()).thenReturn(Arrays.asList("username1", "username2"));
        when(manager.createQuery("SELECT u.userCredentials.username FROM UserEntity u", String.class)).thenReturn(mockQuery);

        List<String> usernames = userService.getListOfUserNames();



        assertEquals(3, usernames.size());
    }

    @Test
    void testSave() {
        // Create and set UserDetails
        UserDetails userDetails = UserDetails.builder()
                .firstName("Adam")
                .lastName("Smith")
                .email("adam@outlook.com")
                .accountNumber("88-88-22")
                .build();

        // Create and set LoginCredentials
        UserCredentials loginCredentials = UserCredentials.builder()
                .username("Adam552")
                .password("password")
                .build();

        // Create and set UserSecurity
        UserSecurity userSecurity = UserSecurity.builder()
                .isAdmin(false)
                .isEnabled(true)
                .role(Role.USER)
                .build();

        // Create and set the UserEntity
        UserEntity userEntity = UserEntity.builder()
                .userDetails(userDetails)
                .userCredentials(loginCredentials)
                .userSecurity(userSecurity)
                .build();

        // Set other necessary fields for the test
        userEntity.setUserID(4);

        // Save the user entity
        userService.save(userEntity);

        // Assertions to verify the service and repository are not null
        assertNotNull(userService);
        assertNotNull(userRepository);
    }

    @Test
    public void testGetUserIDByUserName()
    {
        int userID = 1;

        String testUser = "AKing94";
        int foundID = userService.getUserIDByUserName(testUser);

        assertEquals(userID, foundID);
    }

    @Test
    public void testGetUsersFullNameById_InvalidID(){
        final int userID = -1;

        String name = "Alex King";

        assertThrows(InvalidUserIDException.class, () -> {
            String actual = userService.getUsersFullNameById(userID);
        });
    }

    @Test
    public void testGetUserFullNameById_ValidUserID(){
        final int userID = 1;

        String name = "Alex King";

        String actual = userService.getUsersFullNameById(userID);

        assertEquals(name, actual);
    }

    @Test
    public void testGetUserIDByAccountNumber_emptyAccountNumber(){
        final String emptyAcctNum = "";

        assertThrows(InvalidAccountNumberException.class, () -> {
            userService.getUserIDByAccountNumber(emptyAcctNum);
        });
    }

    @Test
    public void testGetUserIDByAccountNumber_ValidAcctNumber(){
        final String acctNum = "89-42-48";

        final int expectedID = 1;
        final int actual = userService.getUserIDByAccountNumber(acctNum);

        assertEquals(expectedID, actual);
    }

    @Test
    public void testRegisterUser_NullUserDTO(){
        assertThrows(InvalidUserDTOException.class, () -> {
            userService.registerUser(null);
        });
    }

    @Test
    public void testRegisterUser_ValidUserDTO(){

    }

    @Test
    public void testFindByUser(){
        final String user = "AKing94";

        Optional<UserEntity> userEntity = userService.findByUser(user);
        assertNotNull(userEntity);
    }



    @AfterEach
    void tearDown() {
    }
}