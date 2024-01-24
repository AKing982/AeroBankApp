package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.AuthenticationServiceImpl;
import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import com.example.aerobankapp.workbench.tokens.AuthTokenResponse;
import com.example.aerobankapp.workbench.utilities.LoginRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class,
           excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationServiceImpl authenticationManager;


    private AuthController authController;

    @Test
    public void shouldAuthenticateUser() throws Exception
    {
        JWTUtil jwtUtil = new JWTUtil();
        Authentication auth = new UsernamePasswordAuthenticationToken("AKing94", "Halflifer45!");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);

        String expectedToken = "GeneratedToken";
        when(jwtUtil.generateToken(auth)).thenReturn(expectedToken);

        mockMvc.perform(post("/api/auth/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"AKing94\", \"password\": \"Halflifer45!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));

        verify(authenticationManager).authenticate(any(Authentication.class));
    }

    @Test
    public void testAuthenticateUser_ValidCredentials() {
        String username = "AKing94";
        String password = "Halflifer45!";
        LoginRequest loginRequest = new LoginRequest(username, password);
        UserDetails userDetails = User.builder()
                                    .username("AKing94")
                                    .password("Halflifer45!")
                                    .build();


        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Mock the behavior of the authenticationService
        AuthenticationManager authenticationManager1 = Mockito.mock(AuthenticationManager.class);
        when(authenticationManager1.authenticate(Mockito.any())).thenReturn(auth);

        JWTUtil jwtUtil = Mockito.mock(JWTUtil.class);
        String expectedToken = "GeneratedToken";
        when(jwtUtil.generateToken(Mockito.any(Authentication.class))).thenReturn(expectedToken);

        AuthController authController = new AuthController(authenticationManager);

        // Invoke the method
        ResponseEntity<?> responseEntity = authController.createAuthenticateToken(loginRequest);


        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        AuthTokenResponse authTokenResponse = (AuthTokenResponse) responseEntity.getBody();
        assertEquals(expectedToken, authTokenResponse.getToken());
    }

    @Test
    public void shouldReturnUnauthorizedForInvalidCredentials() throws Exception
    {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("Invalid Credentials"));

        mockMvc.perform(post("/api/auth/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"AKing\",\"password\":\"Halflife\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid Credentials"));

        verify(authenticationManager).authenticate(any(Authentication.class));
    }

    @Test
    public void shouldReturnBadRequestForMalformedRequest() throws Exception
    {
        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"AKing94\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSetCorsHeadersForAllowedOrigins() throws Exception {
        mockMvc.perform(post("/api/auth/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"AKing94\",\"password\":\"Halflifer45!\"}")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}