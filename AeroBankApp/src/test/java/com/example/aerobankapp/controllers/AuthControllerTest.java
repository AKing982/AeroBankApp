package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.AuthenticationServiceImpl;
import com.example.aerobankapp.services.PlaidAccountsServiceImpl;
import com.example.aerobankapp.workbench.security.authentication.JWTUtil;
import com.example.aerobankapp.workbench.tokens.AuthTokenResponse;
import com.example.aerobankapp.workbench.utilities.LoginRequest;
import com.example.aerobankapp.workbench.utilities.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class,
           excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationServiceImpl authenticationManager;

    @MockBean
    private PlaidAccountsServiceImpl accountsService;

    @MockBean
    private JWTUtil jwtUtil;

    private AuthController authController;

    @BeforeEach
    public void setup()
    {
        Authentication auth = new UsernamePasswordAuthenticationToken("AKing94", null, Collections.emptyList());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);

        String expectedToken = "mockToken";
        when(jwtUtil.generateToken(any(Authentication.class))).thenReturn(expectedToken);
    }



    @Test
    public void shouldAuthenticateUser() throws Exception
    {

        mockMvc.perform(post("/api/auth/login")
                .content(asJsonString(new LoginRequest("AKing94", "Halflifer45!")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"))
                .andDo(print());

        //verify(authenticationManager).authenticate(any(Authentication.class));
    }

    public static String asJsonString(final Object obj)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(obj);
        }catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAuthenticateUser_ValidCredentials() {
        int userID = 1;
        String username = "AKing94";
        String password = "Halflifer45!";
        LoginRequest loginRequest = new LoginRequest(username, password);
        UserDetails userDetails = User.builder()
                                    .username("AKing94")
                                    .password("Halflifer45!")
                                    .roles(Role.ADMIN.getDescription())
                                    .build();


        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Mock the behavior of the authenticationService
        AuthenticationManager authenticationManager1 = Mockito.mock(AuthenticationManager.class);
        when(authenticationManager1.authenticate(Mockito.any())).thenReturn(auth);

        JWTUtil jwtUtil = Mockito.mock(JWTUtil.class);
        String expectedToken = "eyJhbGciOiJub25lIn0.eyJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4ifQ.";
        when(jwtUtil.generateToken(Mockito.any(Authentication.class))).thenReturn(expectedToken);

       // when(authenticationManager.login(anyString(), anyString())).thenReturn(expectedToken);
        AuthController authController = new AuthController(authenticationManager);

        // Invoke the method
        ResponseEntity<?> responseEntity = authController.createAuthenticateToken(loginRequest);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        AuthTokenResponse authTokenResponse = (AuthTokenResponse) responseEntity.getBody();
        System.out.println(authTokenResponse.toString());
        assertEquals(expectedToken, authTokenResponse.getToken());
        assertNotNull(authTokenResponse);
    }

    @Test
    public void createAuthenticationTokenSuccess()
    {
        LoginRequest loginRequest = new LoginRequest( "AKing94", "Halflifer45!");
        String mockToken = "eyJhbGciOiJub25lIn0.eyJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4ifQ.";

     //   when(authenticationManager.login(anyString(), anyString())).thenReturn(mockToken);

        AuthController authController1 = new AuthController(authenticationManager);
        ResponseEntity<?> response = authController1.createAuthenticateToken(loginRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthTokenResponse);
        assertEquals(mockToken, ((AuthTokenResponse)response.getBody()).getToken());

        verify(authenticationManager, times(1)).login("AKing94", "Halflifer45!");

    }

    @Test
    public void shouldReturnUnauthorizedForInvalidCredentials() throws Exception
    {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("Invalid Credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .content(asJsonString(new LoginRequest("AKing9", "Halflifer4")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"))
                .andDo(print());

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