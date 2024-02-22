package com.example.aerobankapp.email;

import com.example.aerobankapp.configuration.EmailTestConfig;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit38ClassRunner.class)
@SpringBootTest
class EmailSenderImplTest
{


    private EmailServiceImpl emailService;

    @Mock
    private EmailConfig emailConfig;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl(emailConfig);
    }

    @Test
    public void testNullEmailConstructor()
    {
        assertThrows(NullPointerException.class, () -> {
            emailService = new EmailServiceImpl(null);
        });
    }

    @Test
    public void testValidEmail() {
        // Create a real instance of EmailServiceImpl with mocked dependencies
        EmailService emailService = new EmailTestConfig().configureEmailServiceForTesting();

        String toEmail = "massgamer1121@gmail.com";
        String fromEmail = "alex@utahkings.com";
        String subject = "Test Email";
        String text = "Hello from Email";

        // Act
        // Assuming sendEmail returns a boolean indicating success/failure
        boolean result = emailService.sendEmail(toEmail.trim(), fromEmail.trim(), subject.trim(), text.trim());

        // Assert
        assertTrue(result); //
    }

    @Test
    public void testInvalidEmail()
    {

    }



    @AfterEach
    void tearDown() {
    }
}