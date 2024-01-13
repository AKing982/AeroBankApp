package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.UserDTO;
import com.example.aerobankapp.repositories.UserRepository;

import com.example.aerobankapp.workbench.utilities.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("com.example.aerobankapp")
class UserServiceImplTest {

    @MockBean
    private UserDAOImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Mock
    private EntityManager manager;
    private UserDTO user1;
    private UserEntity test;

    @BeforeEach
    void setUp()
    {
        user1 = UserDTO.builder()
                .userName("AKing94")
                .email("alex@utahkings.com")
                .password("pass")
                .pinNumber("5988")
                .build();

        test = UserEntity.builder()
                .isAdmin(true)
                .username("AKing94")
                .email("alex@utahkings.com")
                .password("Halflifer94!")
                .pinNumber("5988")
                .role(Role.valueOf("ADMIN"))
                .isEnabled(true)
                .id(9)
                .build();
        userService = new UserDAOImpl(userRepository, manager);


    }

    @Test
    public void testFindAll()
    {


        List<UserEntity> users = new ArrayList<>();
        users.add(test);
        List<UserEntity> result = userService.findAll();

        assertNotNull(userService);
        assertNotNull(manager);
        assertNotNull(userRepository);
        assertEquals(users, result);
    }

    @Test
    public void testSave()
    {

     //   List<UserEntity> foundUser = userService.findByUserName("AKing94");

        userService.save(test);

        assertNotNull(userRepository);
        assertNotNull(userService);
        assertEquals(1, userService.findAll().size());
    }

    @Test
    public void testDeleteUser()
    {

        userService.delete(test);

        List<UserEntity> allUsers = userService.findAll();

        assertEquals(0, allUsers.size());
    }

    @Test
    public void testFindAllById()
    {

        userService.save(test);

        UserEntity allUsers = userService.findAllById(1L);

        assertNotNull(allUsers);
        assertEquals(test, allUsers);
    }

    @Test
    public void testFindByUserName()
    {
        List<UserEntity> allUsers = userService.findByUserName("AKing94");

        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
    }


    @AfterEach
    void tearDown() {
    }
}