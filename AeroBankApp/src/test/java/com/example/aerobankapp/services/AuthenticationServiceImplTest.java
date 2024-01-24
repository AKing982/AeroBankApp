package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AuthenticationServiceImplTest {

    @Qualifier("userDetailsServiceImpl")
    @Mock
    @Autowired
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void authenticateSuccessfully() {
        // Set up
        String username = "AKing94";
        String password = "Halflifer45!";
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn(username);
        when(mockUserDetails.getPassword()).thenReturn(password);
        Mockito.lenient().when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        Mockito.lenient().when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        // Action
        Authentication result = authenticationService.authenticate(authentication);


        // Assert
        assertNotNull(result);
        assertEquals(username, result.getName());
    }

    @Test
    void authenticateWithBadCredentials() {
        // Set up
        String username = "AKing";
        String wrongPassword = "Halflifer";
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn(username);
        when(mockUserDetails.getPassword()).thenReturn("Halflifer");
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, wrongPassword);

        // Action & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.authenticate(authentication);
        });
    }

    @AfterEach
    void tearDown() {
    }
}