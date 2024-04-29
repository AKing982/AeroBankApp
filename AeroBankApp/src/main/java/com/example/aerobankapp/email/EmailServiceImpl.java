package com.example.aerobankapp.email;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImpl implements EmailService
{
    private final EmailConfig emailConfig;
    private final SpringTemplateEngine springTemplateEngine;
    private Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    public EmailServiceImpl(EmailConfig config, SpringTemplateEngine springTemplateEngine)
    {
        Objects.requireNonNull(config, "Config cannot be null");
        this.emailConfig = config;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    @Async
    public CompletableFuture<Boolean> sendEmail(String toEmail, String fromEmail, String templateName, Context context, String subject)
    {
        try
        {
            Session session1 = createSession();
            LOGGER.info("Sending to: {}", toEmail);
            LOGGER.info("Sender Email: {}", fromEmail);

            String body = springTemplateEngine.process(templateName, context);

            MimeMessage mimeMessage = createMessage(session1, toEmail.trim(), fromEmail.trim(), body, subject);
            Transport.send(mimeMessage);

            LOGGER.info("Email sent successfully to {}", toEmail);
            return CompletableFuture.completedFuture(true);

        }catch(MessagingException mg)
        {
            LOGGER.error("Error sending email: {} ", mg.getMessage());
            LOGGER.error(Arrays.toString(mg.getStackTrace()));
            return CompletableFuture.completedFuture(false);
        }
    }

    private Properties getSystemProperties()
    {
        return System.getProperties();
    }


    private Properties configureMailServerProperties()
    {

        Properties mailServerProperties = getSystemProperties();
        mailServerProperties.put("mail.smtp.host", emailConfig.getHost());
        mailServerProperties.put("mail.smtp.port", String.valueOf(emailConfig.getPort()));
        mailServerProperties.put("mail.smtp.auth", String.valueOf(emailConfig.isAuthenticationRequired()));

        if(emailConfig.isUseTLS())
        {
            mailServerProperties.put("mail.smtp.starttls.enable", "true");
        }
        if(emailConfig.isUseTLS())
        {
            mailServerProperties.put("mail.smtp.socketFactory.port", String.valueOf(emailConfig.getPort()));
            mailServerProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        return mailServerProperties;
    }

    private Session createSession()
    {
        Properties properties = configureMailServerProperties();
        if(emailConfig.isAuthenticationRequired())
        {
            return Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailConfig.getUsername(), emailConfig.getPassword());
                }
            });
        }

        return Session.getInstance(properties);
    }

    private MimeMessage createMessage(Session session, String toEmail, String fromEmail, String body, String subject) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject);
        message.setContent(body, "text/html");
        return message;
    }

}
