package com.example.aerobankapp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;



import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CsrfTokenGenerationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void postRequestWithoutCsrfTokenIsRejected() throws Exception
    {
        mockMvc.perform(post("/api/auth/login"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void postRequestWithCsrfTokenIsAccepted() throws Exception
    {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        CsrfToken csrfToken = new HttpSessionCsrfTokenRepository().generateToken(new MockHttpServletRequest());
        
        mockMvc.perform(post("/api/auth/login")
                .sessionAttr(CsrfToken.class.getName(), csrfToken)
                .header("X-CSRF-TOKEN", csrfToken.getToken()))
                .andExpect(status().isOk());
    }


}