package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.EmailServerEntity;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class EmailServerServiceImplTest
{
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EmailServerServiceImpl emailServerService;

    @Mock
    private TypedQuery<Long> typedQuery;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEmailServerByIdFound() {
        Long id = 1L;
        EmailServerEntity mockEntity = new EmailServerEntity();
        mockEntity.setId(id);
        mockEntity.setPort(1025);
        mockEntity.setHost("localhost");
        mockEntity.setUsername("sa");
        mockEntity.setPassword("pass");
        mockEntity.setTLS(false);

        TypedQuery<EmailServerEntity> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(EmailServerEntity.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("id", id)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(2)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(mockEntity));

        EmailServerEntity result = emailServerService.getEmailServerById(id).get(0);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(mockEntity.getPort(), result.getPort());
        assertEquals(mockEntity.getHost(), result.getHost());
        assertEquals(mockEntity.getUsername(), result.getUsername());
        assertEquals(mockEntity.getPassword(), result.getPassword());
    }

    @Test
    void getEmailServerByIdNotFound() {
        Long id = 1L;

        TypedQuery<EmailServerEntity> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(EmailServerEntity.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("id", id)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(2)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList());

        EmailServerEntity result = emailServerService.getEmailServerById(id).get(0);

        assertNull(result);
    }

    @Test
    public void testEmailServerExists_WhenExists() {
        // Arrange
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(1L); // Mock the count to be 1

        // Act
        boolean exists = emailServerService.emailServerExists("localhost", 1025);

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testEmailServerExists_WhenNotExists() {
        // Arrange
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(0L); // Mock the count to be 0

        // Act
        boolean exists = emailServerService.emailServerExists("localhost", 1025);

        // Assert
        assertFalse(exists);
    }

    @AfterEach
    void tearDown() {
    }
}