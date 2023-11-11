package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.Users;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.security.authentication.UserAuthority;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private EntityManager manager;
    private User user1;
    private Users test;

    @BeforeEach
    void setUp()
    {
        user1 = User.builder()
                .user("AKing94")
                .userAuthority(UserAuthority.createUserAuthority())
                .id(1)
                .email("alex@utahkings.com")
                .password("pass")
                .pinNumber(5988)
                .accountNumber("12-22-42")
                .build();

        test = Users.builder()
                .isAdmin(true)
                .username("AKing94")
                .email("alex@utahkings.com")
                .password("Halflifer94!")
                .pinNumber("5988")
                .build();

    }

    @Test
    public void testFindAll()
    {
        userService = new UserServiceImpl(userRepository, manager);

        List<Users> users = new ArrayList<>();
        users.add(test);
        userService.save(test);

        List<Users> result = userService.findAll();

        assertEquals(users, result);
    }

    @Test
    public void testSave()
    {

        userService = new UserServiceImpl(userRepository, manager);
        userService.save(test);
        List<Users> foundUser = userService.findByUserName("AKing94");

        assertNotNull(userRepository);
        assertNotNull(userService);
        assertEquals(test, foundUser.get(0));
    }

    @Test
    public void testDeleteUser()
    {
        userService = new UserServiceImpl(userRepository, manager);
        userService.delete(test);

        List<Users> allUsers = userService.findAll();

        assertTrue(allUsers.isEmpty());
    }

    @Test
    public void testFindAllById()
    {
        userService = new UserServiceImpl(userRepository, manager);
        userService.save(test);

        Users allUsers = userService.findAllById(1L);

        assertNotNull(allUsers);
        assertEquals(test, allUsers);
    }

    @Test
    public void testFindByUserName()
    {
        userService = new UserServiceImpl(userRepository, manager);
        List<Users> allUsers = userService.findByUserName("AKing94");

        assertNotNull(allUsers);
        assertEquals(test, allUsers.get(0));
    }


    @AfterEach
    void tearDown() {
    }
}