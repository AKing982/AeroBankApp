package com.example.aerobankapp.workbench.model;

import com.example.aerobankapp.workbench.security.authentication.SecurityConfig;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.common.util.report.qual.ReportUnqualified;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class LoginModelTest
{
    @MockBean
    private LoginModel loginModel;

    @Autowired
    private SecurityConfig securityConfig;

    private String user;

    private String password;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    public void testUserNameWithConstructor()
    {
        user = "AKing94";
        password = "Halflifer94!";
        loginModel = new LoginModel(user, password);

        assertNotNull(loginModel);
        assertEquals(user, loginModel.getUsername());
        assertEquals(password, loginModel.getPassword());
        assertNotEquals(password, loginModel.getEncodedPassword());
    }

    @Test
    public void testEncodedCredentialsMap()
    {
        user = "AKing94";
        password = "Halflifer94!";
        loginModel = new LoginModel(user, password);
        String userKey = user;

        Map<String, String> encodedCredentials = loginModel.getEncodedCredentialsMap();

        String passwordValue = encodedCredentials.entrySet().stream()
                        .filter(entry -> userKey.equals(entry.getKey()))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElse(null);

        System.out.println(passwordValue);
        assertNotNull(encodedCredentials);
        assertEquals(passwordValue, encodedCredentials.get(user));
        assertNotEquals(password, passwordValue);
    }

    @Test
    public void testEncoding()
    {
        user = "AKing94";
        password = "Halflifer94!";
        loginModel = new LoginModel(user, password);
        boolean isEncoded = loginModel.isEncoded();

        String encodedPassword = loginModel.getEncodedPassword();

        assertNotEquals(password, encodedPassword);
        assertTrue(isEncoded);
    }

    @Test
    public void testPasswordFromMap()
    {
        user = "AKing94";
        password = "Halflifer94!";
        loginModel = new LoginModel(user, password);

        String passwordFromMap = loginModel.getPasswordFromMap();

        assertNotNull(passwordFromMap);
        assertNotEquals(password, passwordFromMap);
    }

    @AfterEach
    void tearDown() {
    }
}