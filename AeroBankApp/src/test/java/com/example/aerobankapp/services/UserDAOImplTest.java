package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.utilities.Role;
import com.example.aerobankapp.workbench.utilities.UserType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserDAOImplTest
{
    @MockBean
    private UserDAOImpl userDAO;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setUp()
    {
        userDAO = new UserDAOImpl(userRepository, entityManager);
    }

    @Test
    public void saveUser()
    {
        // Arrange
        user = UserEntity.builder()
                .email("alex@utahkings.com")
                .isAdmin(true)
                .password("Halflifer45!")
                .pinNumber("5988")
                .username("AKing94")
                .isEnabled(true)
                .id(1)
                .role(Role.valueOf("Admin"))
                .build();

        // Act
        userDAO.save(user);

        // Assert
        assertEquals(1, userDAO.findAll().size());
    }

    @AfterEach
    void tearDown() {
    }
}