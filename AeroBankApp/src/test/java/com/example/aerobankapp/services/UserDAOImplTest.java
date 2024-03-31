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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest

class UserDAOImplTest
{
    @MockBean
    private UserServiceImpl userDAO;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setUp()
    {
        userDAO = new UserServiceImpl(userRepository, entityManager);
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
                .userID(1)
                .role(Role.ADMIN)
                .build();

        // Act
        userDAO.save(user);

        // Assert
        assertEquals(1, userDAO.findAll().size());
    }

    @Test
    @Transactional
    public void testFindByUserNameWithValidUser()
    {
        String user = "AKing94";

        List<UserEntity> userEntities = userDAO.findByUserName(user);
        UserEntity foundUser = userEntities.get(0);

        assertEquals(user, foundUser.getUsername());
    }

    @Test
    public void testDoesAccountNumberExist_EmptyAccountNumber(){
        final String accountNumber = "";

        boolean result = userDAO.doesAccountNumberExist(accountNumber);

        assertFalse(result);
    }

    @Test
    public void testDoesAccountNumberExist_ValidAccountNumber(){
        final String accountNumber = "89-42-48";

        boolean result = userDAO.doesAccountNumberExist(accountNumber);

        assertTrue(result);
    }




    @AfterEach
    void tearDown() {
    }
}