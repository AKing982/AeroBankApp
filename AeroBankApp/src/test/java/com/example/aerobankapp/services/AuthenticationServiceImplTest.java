package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthenticationServiceImplTest {

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private UserEntity users;



    @BeforeEach
    void setUp()
    {
        authenticationService = new AuthenticationServiceImpl();
        userService = new UserServiceImpl(userRepository, entityManager);

        users = UserEntity.builder()
                .id(1)
                .isAdmin(true)
                .email("alex@utahkings.com")
                .pinNumber("5988")
                .username("AKing94")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .build();


    }

    @Test
    public void testAuthenticateByUserCount()
    {
        String user = "AKing94";
        String pass = "Halflifer94!";
        userService.save(users);


    }

    @AfterEach
    void tearDown() {
    }
}