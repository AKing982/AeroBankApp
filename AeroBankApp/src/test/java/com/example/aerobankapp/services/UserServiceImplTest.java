package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
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
                .pinNumber(pin)
                .password(password)
                .email(email)
                .username(username)
                .accountNumber(expectedAccountNumber)
                .role(role).build();

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
        when(manager.createQuery("SELECT u.username FROM UserEntity u", String.class)).thenReturn(mockQuery);

        List<String> usernames = userService.getListOfUserNames();



        assertEquals(3, usernames.size());
    }

    @Test
    void testSave() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("Adam552");
        userEntity.setPassword("password");
        userEntity.setAccountNumber("37-23-21");
        userEntity.setPinNumber("2222");
        userEntity.setEmail("adam@outlook.com");
        userEntity.setEnabled(true);
        userEntity.setRole(Role.USER);
        userEntity.setUserID(4);
        // Set other necessary fields...

        userService.save(userEntity);

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



    @AfterEach
    void tearDown() {
    }
}