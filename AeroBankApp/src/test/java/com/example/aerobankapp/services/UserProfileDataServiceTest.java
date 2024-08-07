package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.UserProfileDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserProfileDataServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Object[]> typedQuery;

    @InjectMocks
    private UserProfileDataService userProfileDataService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRunUserProfileQuery() {
        // Arrange
        int userId = 1;
        Object[] queryResult = new Object[]{"John Doe", "john@example.com", LocalDateTime.of(2024, 6, 22, 11, 31)};
        when(entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyInt(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(queryResult);

        // Act
        Optional<UserProfileDTO> result = userProfileDataService.runUserProfileQuery(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().name());
        assertEquals("john@example.com", result.get().email());
        assertEquals("11:31 AM on 06/22/2024", result.get().lastLogin());
    }

    @Test
    void testExecuteQuery() {
        // Arrange
        String jpql = "SELECT CONCAT(u.firstName, ' ', u.lastName), u.email, ul.lastLogin FROM UserEntity u JOIN u.userLogEntity ul WHERE u.userID = ?1";
        Object[] queryResult = new Object[]{"Jane Doe", "jane@example.com", LocalDateTime.of(2024, 6, 23, 10, 30)};
        when(entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyInt(), any())).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(queryResult);

        // Act
        Optional<UserProfileDTO> result = userProfileDataService.executeQuery(jpql, 2);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Jane Doe", result.get().name());
        assertEquals("jane@example.com", result.get().email());
        assertEquals("10:30 AM on 06/23/2024", result.get().lastLogin());
    }

    @Test
    void testExecuteQueryForList() {
        // Arrange
        String jpql = "SELECT CONCAT(u.firstName, ' ', u.lastName), u.email, ul.lastLogin FROM UserEntity u JOIN u.userLogEntity ul";
        List<Object[]> queryResults = Arrays.asList(
                new Object[]{"John Doe", "john@example.com", LocalDateTime.of(2024, 6, 22, 11, 31)},
                new Object[]{"Jane Doe", "jane@example.com", LocalDateTime.of(2024, 6, 23, 10, 30)}
        );
        when(entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyInt(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(queryResults);

        // Act
        List<UserProfileDTO> results = userProfileDataService.executeQueryForList(jpql);

        // Assert
        assertEquals(2, results.size());
        assertEquals("John Doe", results.get(0).name());
        assertEquals("john@example.com", results.get(0).email());
        assertEquals("11:31 AM on 06/22/2024", results.get(0).lastLogin());
        assertEquals("Jane Doe", results.get(1).name());
        assertEquals("jane@example.com", results.get(1).email());
        assertEquals("10:30 AM on 06/23/2024", results.get(1).lastLogin());
    }

    @Test
    void testExecuteQueryReturnsEmptyOptionalOnException() {
        // Arrange
        String jpql = "SELECT CONCAT(u.firstName, ' ', u.lastName), u.email, ul.lastLogin FROM UserEntity u JOIN u.userLogEntity ul WHERE u.userID = ?1";
        when(entityManager.createQuery(anyString(), eq(Object[].class))).thenThrow(new RuntimeException("Database error"));

        // Act
        Optional<UserProfileDTO> result = userProfileDataService.executeQuery(jpql, 1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteQueryForListReturnsEmptyListOnException() {
        // Arrange
        String jpql = "SELECT CONCAT(u.firstName, ' ', u.lastName), u.email, ul.lastLogin FROM UserEntity u JOIN u.userLogEntity ul";
        when(entityManager.createQuery(anyString(), eq(Object[].class))).thenThrow(new RuntimeException("Database error"));

        // Act
        List<UserProfileDTO> results = userProfileDataService.executeQueryForList(jpql);

        // Assert
        assertTrue(results.isEmpty());
    }

    @AfterEach
    void tearDown() {
    }
}