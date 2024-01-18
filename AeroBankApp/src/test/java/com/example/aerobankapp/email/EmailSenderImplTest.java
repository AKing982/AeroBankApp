package com.example.aerobankapp.email;

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
    public void testValidEmail()
    {

        when(emailConfig.getHost()).thenReturn("zimbra.xmission.com");
        when(emailConfig.getPort()).thenReturn(587);

        String toEmail = "massgamer1121@gmail.com";
        String fromEmail = "alex@utahkings.com";
        String subject = "Test Email";
        String text = "Hello from Email";

        EmailConfig emailConfig = EmailConfig.builder()
                .host("zimbra.xmission.com")
                .password("aLEXfORGOT24!")
                .useSSL(false)
                .useTLS(false)
                .port(587)
                .username("alex@utahkings.com")
                .build();

        emailService.sendEmail(toEmail, fromEmail, text, subject);
        verify(emailConfig, times(1)).getHost();

    }



    @AfterEach
    void tearDown() {
    }
}