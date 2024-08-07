package com.example.aerobankapp.email;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.EmailTestConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import jakarta.mail.MessagingException;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({JpaConfig.class, AppConfig.class})
class EmailSenderImplTest
{


    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private EmailConfig emailConfig;

    @Mock
    private SpringTemplateEngine templateEngine;

    private EmailConfig createMailHogEmailConfig() {
        EmailConfig config = new EmailConfig();
        config.setHost("localhost");
        config.setPort(1025);  // Default MailHog SMTP port
        config.setAuthenticationRequired(false);  // MailHog doesn't require authentication
        return config;
    }

    @BeforeEach
    void setUp() {
        EmailConfig mailHogConfig = createMailHogEmailConfig();
        emailService = new EmailServiceImpl(mailHogConfig, templateEngine);

    }

    @Test
    public void testNullEmailConstructor() {
        assertThrows(NullPointerException.class, () -> new EmailServiceImpl(null, templateEngine));
        assertThrows(NullPointerException.class, () -> new EmailServiceImpl(emailConfig, null));
    }

    @Test
    public void testValidEmail() throws ExecutionException, InterruptedException {
        String toEmail = "alex@utahkings.com";
        String fromEmail = "brutef28@gmail.com";
        String templateName = "testTemplate";
        String subject = "Test Email";
        Context context = new Context();
        context.setVariable("name", "Test User");

        when(templateEngine.process(eq(templateName), any(Context.class))).thenReturn("Processed email body");

        CompletableFuture<Boolean> result = emailService.sendEmail(toEmail, fromEmail, templateName, context, subject);

        assertTrue(result.get());
        verify(templateEngine).process(eq(templateName), any(Context.class));
    }

    @Test
    public void testInvalidEmail() throws ExecutionException, InterruptedException {
        String toEmail = "test@example.com";
        String fromEmail = "no-reply@example.com";
        String templateName = "testTemplate";
        String subject = "Test Email";
        Context context = new Context();
        context.setVariable("name", "Test User");

        when(templateEngine.process(eq(templateName), any(Context.class))).thenReturn("Processed email body");
        doThrow(new MessagingException("Failed to send email")).when(emailService).sendEmail(toEmail, fromEmail, templateName, context, subject);

        CompletableFuture<Boolean> result = emailService.sendEmail(toEmail, fromEmail, templateName, context, subject);

        assertFalse(result.get());
        verify(templateEngine).process(eq(templateName), any(Context.class));
    }

    @Test
    public void sendVerificationCodeEmail() throws ExecutionException, InterruptedException {
        String toEmail = "user@example.com";
        String fromEmail = "noreply@company.com";
        String subject = "Your Verification Code";
        String verificationCode = "805723";  // This would usually be dynamically generated

        // Prepare the email body using Thymeleaf template engine
        Context context = new Context();
        context.setVariable("code", verificationCode);
        String templateName = "verificationCodeTemplate";
        when(templateEngine.process(eq(templateName), any(Context.class))).thenReturn("Your verification code is: " + verificationCode);

        CompletableFuture<Boolean> result = emailService.sendEmail(toEmail, fromEmail, templateName, context, subject);

        assertTrue(result.get(), "Email should be sent successfully");
        // Verify that the template engine was called with the correct parameters
        verify(templateEngine).process(eq(templateName), any(Context.class));
    }


    @AfterEach
    void tearDown() {
    }
}