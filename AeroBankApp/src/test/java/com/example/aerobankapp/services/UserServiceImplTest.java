package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.UserDTO;
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
                .isEnabled(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isAccountNonExpired(true)
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

        List<UserEntity> foundUser = userService.findByUserName("AKing94");

        assertNotNull(userRepository);
        assertNotNull(userService);
        assertEquals(test, foundUser.get(0));
    }

    @Test
    public void testDeleteUser()
    {

        userService.delete(test);

        List<UserEntity> allUsers = userService.findAll();

        assertTrue(allUsers.isEmpty());
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
        assertEquals(test, allUsers.get(0));
    }


    @AfterEach
    void tearDown() {
    }
}